package com.swiggy.scala

import com.swiggy.scala.SwiggyDB.{Swiggy_Food,saveDataIntoDB}
import com.swiggy.scala.Swiggy_Location_DB.{Swiggy_Location, saveDataIntoLocationDB}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.collection.JavaConverters._

object Swiggy{

  def Crawler(Hit_Url:String):Document= {
    val response: Document =  Jsoup.connect(Hit_Url)
                              .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                              .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                              .header("Accept-Language", "en-US")
                              .header("Host", "www.swiggy.com")
                              .timeout(10000)
                              .get();
    return response;
  }

  def init(function:String=>Document,string:String):Document = {
    val Base_Url:String = string.toString;
    val Doc:Document = Crawler(Base_Url);
    return Doc;
  }

  // Getting All Location Details From Home Page
  def getAllLocationDetails={

    val HomeUrl:String = "https://www.swiggy.com/#city-links";

    var Doc:Document = null;
    Doc = init(Crawler, HomeUrl);
    val responstText: String = Doc.toString;
    val Specs = Doc.getElementsByClass("_3TjLz b-Hy9").asScala;

    for(de <- Specs)println(de.text())
    for(details <- Specs){
      val Name:String = details.text().toString.trim;
      val url:String = s"https://www.swiggy.com/$Name";
      val data = Swiggy_Location(Name.capitalize,url);
      saveDataIntoLocationDB(data);
    }
  }

  //Getting All Urls From DB and Hitting urls

  def getLocationAndFetchDetails():List[String] ={

     val collection = Swiggy_Location_DB.collection;
     var swiggy = collection.find;
     var Location_url:List[String] = List()

     //var MainUrls:List[String] = List()
    for(s <- swiggy){
     var url = s.get("Url");
     Location_url = url.toString::Location_url;
    }
    println(Location_url)

    return (Location_url.reverse)
  }

  def extractDataFromTags(func:(Document,String,String)=>List[String],Doc:Document,tagName:String,tagType:String):List[String]={
    var tempList:List[String] = func(Doc,tagName,tagType);
    return tempList;
  }

  def fetchData(htmlDoc:Document,name:String,strType:String):List[String]={
    var tempList:List[String] = List()

    if(strType.equals("Class")){
      val tempData = htmlDoc.getElementsByClass(name).asScala;
      for(name <- tempData) tempList = name.text().toString.trim::tempList;
      tempList = tempList.reverse;
    }
    return tempList;
  }

  def main(args:Array[String]): Unit ={
    var Name: String = null;
    var BrandName: String = null;
    var Rating: String = null;
    var Price: String = null;
    var Specstext:String = null;

   // getAllLocationDetails;
    val all_urls:List[String] = getLocationAndFetchDetails;

    for(url <- all_urls){
      var Doc:Document = null
      Doc = init(Crawler, url);
      //fetchData(Doc)
      val hotelNames:List[String] = extractDataFromTags(fetchData, Doc,"nA6kb","Class")
      val hotelType:List[String] = extractDataFromTags(fetchData, Doc,"_1gURR","Class")
      val rating:List[String] = extractDataFromTags(fetchData, Doc,"_3Mn31","Class")
      val mRating:List[String] = rating.map(_.split("•")(0).toString.trim)

        for(i <- 0 to hotelNames.length-1){
          val hname:String = hotelNames(i).toString.trim
          val htype:String = hotelType(i).toString.trim
          val hrate:String = mRating(i).toString.trim

          val data = Swiggy_Food(hname,htype,hrate)
          saveDataIntoDB(data)
        }
        println("Done")

    }

  }

}
