package com.zomato.scala

import com.zomato.scala.Zomato_DB.{Zomato_Food, saveDataIntoDB}
import com.zomato.scala.Zomato_Location_DB.{Zomato_Location,saveDataIntoLocationDB}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.collection.JavaConverters._

object Zomato{

  def Crawler(Hit_Url:String):Document= {
    val response: Document =  Jsoup.connect(Hit_Url)
                              .userAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36")
                              .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                              .header("Accept-Language", "en-US")
                              .header("Host", "www.zomato.com")
                              .header("Referer","https://www.zomato.com/")
                              .timeout(50000)
                              .get();
    return response;
  }

  def init(function:String=>Document,string:String):Document = {
    val Base_Url:String = string.toString;
    val Doc:Document = Crawler(Base_Url);
    return Doc;
  }

  def fetchStateUrls(Doc:Document):List[String]={

    var stateUrls:List[String] = List()
    stateUrls = extractDataFromTags(fetchData, Doc,"col-l-1by3 col-s-8 pbot0","Class",true)

    return stateUrls
  }

  // Getting All Location Details From Home Page
  def getAllLocationDetails={

    val HomeUrl:String = "https://www.zomato.com/india";

    var Doc:Document = null;
    Doc = init(Crawler, HomeUrl);
    val responstText: String = Doc.toString;
    val Specs = Doc.getElementsByClass("ui segment flex").asScala;

   // for(de <- Specs)println(de.getElementsByTag("a").attr("href"))
    for(details <- Specs){
      val Name:String = details.text().toString.trim;
      val url:String = details.getElementsByTag("a").attr("href")
      //val data = Zomato_Location(Name.capitalize,url);
      //saveDataIntoLocationDB(data);
      val TempList:List[String] = fetchStateUrls(init(Crawler,url))
      for(j <- TempList){
        val urls = j.toString.trim
        val data = Zomato_Location(Name.capitalize,urls)
        saveDataIntoLocationDB(data);
      }
    }
  }


  //Getting All Urls From DB and Hitting urls

  def getLocationAndFetchDetails():List[String] ={

    val collection = Zomato_Location_DB.collection;
    var swiggy = collection.find;
    var Location_url:List[String] = List()

    for(s <- swiggy){
      var url = s.get("Url");
      Location_url = url.toString::Location_url;
    }
    println(Location_url)

    return (Location_url.reverse)
  }

  def extractDataFromTags(func:(Document,String,String,Boolean)=>List[String],Doc:Document,tagName:String,tagType:String,flagType:Boolean):List[String]={
    var tempList:List[String] = func(Doc,tagName,tagType,flagType);
    return tempList;
  }

  def fetchData(htmlDoc:Document,name:String,strType:String,hrefFlag:Boolean):List[String]={
    var tempList:List[String] = List()

    if(strType.equals("Class")){
      val tempData = htmlDoc.getElementsByClass(name).asScala;
      if(hrefFlag){
        for(href <- tempData) tempList = href.getElementsByTag("a").attr("href").toString.trim::tempList;tempList = tempList.reverse;
      }else {
        for (name <- tempData) tempList = name.text().toString.trim :: tempList; tempList = tempList.reverse;
      }
    }
    return tempList;
  }

  def main(args:Array[String]): Unit ={
    var Name: String = null;
    var BrandName: String = null;
    var Rating: String = null;
    var Price: String = null;
    var Specstext:String = null;

    //getAllLocationDetails;
    val all_urls:List[String] = getLocationAndFetchDetails;

    for(url <- all_urls){
      var Doc:Document = null
      Doc = init(Crawler, url);
      //fetchData(Doc)
      val hotelNames:List[String] = extractDataFromTags(fetchData, Doc,"res_name","Class",false)
      val hotelType:List[String] = extractDataFromTags(fetchData, Doc,"grey-text description","Class",false)
      val rating:List[String] = extractDataFromTags(fetchData, Doc,"col-l-1by3 col-s-1by3  pr10 pl10 ","Class",false)
      val mRating:List[String] = rating.map(_.split("•")(0).toString.trim)

      for(i <- 0 to hotelNames.length-1){
        val hname:String = hotelNames(i).toString.trim
        val htype:String = hotelType(i).toString.trim
        val hrate:String = mRating(i).split(" ")(0).toString.trim

        val data = Zomato_Food(hname,htype,hrate)
        saveDataIntoDB(data)

      }
      println("Done")

    }

  }

}

