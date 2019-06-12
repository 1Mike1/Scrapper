package com.amazon.scala

import com.amazon.scala.MongoFactory.{Amazon, saveDataIntoDB}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.collection.JavaConverters._

object BaseScrapper{

  def Crawler(Hit_Url:String):Document = {
    val response: Document = Jsoup.connect(Hit_Url)
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                            .header("Accept-Language", "en-US")
                            .header("Host", "www.amazon.com")
                            .timeout(10000)
                            .get();
    return response;
  }

  def init(function:String=>Document,string:String):Document = {
    val Base_Url:String = string.toString;
    val Doc:Document = Crawler(Base_Url);
    return Doc;
  }

//  def insertDataToMongoDB(mongoDb:(String,String,String,String,String) => MongoDBObject,
//                          Name:String,Brand:String,Rating:String,Price:String,Specs:String) = mongoDb(Name,Brand,Rating,Price,Specs);

  def main(args:Array[String]): Unit ={
    var Name: String = null;
    var BrandName: String = null;
    var Rating: String = null;
    var Price: String = null;
    var Specstext:String = null;

    val urls:List[String] = List("https://www.amazon.com/AmazonBasics-Full-Size-Ergonomic-Wireless-Scrolling/dp/B078HFRNSP/ref=lp_12246044011_1_1?srs=12246044011&ie=UTF8&qid=1551167384&sr=8-1","https://www.amazon.com/Apple-Silicone-Case-iPhone-Max/dp/B07H9PDLRF/ref=sr_1_1_sspa?keywords=iphone+xs+max&qid=1551168896&s=gateway&sr=8-1-spons&psc=1");

    try {

      for(url <- urls) {
        var Doc:Document = null;
        //println(url)
        //val url = "https://www.amazon.com/Apple-Silicone-Case-iPhone-Max/dp/B07H9PDLRF/ref=sr_1_1_sspa?keywords=iphone+xs+max&qid=1551168896&s=gateway&sr=8-1-spons&psc=1"
        Doc = init(Crawler, url);
        val responstText: String = Doc.toString;

        Name = Doc.getElementById("productTitle").text().toString.trim;
        //println(Name)

        BrandName = Doc.getElementById("bylineInfo").text().toString.trim;
        //For Capturing Multiple Data From Class
        //      val star = Doc.getElementsByClass("a-icon-alt").asScala
        //      val starText = star.map(tagname => tagname.text())
        //
        //      println(starText)

        Rating = Doc.getElementsByClass("a-icon-alt").first().text().trim;
        println(Rating);

        val Specs = Doc.getElementsByClass("a-unordered-list a-vertical a-spacing-none").asScala;
        Specstext = Specs.map(spcs => spcs.getElementsByTag("li").text().toString).mkString(" ").trim;
        println(Specstext.toString);

        try{
        Price = Doc.getElementById("priceblock_ourprice").text().toString.replace("$", "").toString.trim;
        println(Price);
        }catch {
          case e:Exception => Price="";println(e);
        }

        val data = Amazon(Name, BrandName, Rating, Price, Specstext);
        saveDataIntoDB(data);
        println("Data Inserted");
      }
    } catch {
      case e: Exception => println(e);
    }

  }

}
