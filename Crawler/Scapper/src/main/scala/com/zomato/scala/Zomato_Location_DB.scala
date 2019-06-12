package com.zomato.scala

import com.mongodb.casbah.Imports.{MongoDBObject, _}
import com.mongodb.casbah.MongoConnection

object Zomato_Location_DB{
  private val SERVER = "localhost"
  private val PORT   = 27017
  private val DATABASE = "Food"
  private val COLLECTION = "Zomato_Location"

  val connection = MongoConnection(SERVER)
  val collection = connection(DATABASE)(COLLECTION)
  //println(collection)

  case class Zomato_Location(Location: String,Url:String)

  def buildMongoDbObject(zomato_loc: Zomato_Location): MongoDBObject = {
    val builder = MongoDBObject.newBuilder;
    builder += "Location" -> zomato_loc.Location;
    builder += "Url" -> zomato_loc.Url;
    //    builder += "Rating" -> amazon.Rating;
    //    builder += "Price" -> amazon.Price;
    //    builder += "Specification" -> amazon.Specs;
    //println(builder.result)
    builder.result;
  }

  // our 'save' method
  def saveDataIntoLocationDB(zomato_loc: Zomato_Location) {
    val mongoObj = buildMongoDbObject(zomato_loc)
    Zomato_Location_DB.collection.save(mongoObj)
  }

  //    def main(args:Array[String]): Unit ={
  //      val apple = Swiggy_Location("ios", "Apple")
  //      val google = Swiggy_Location("Android","Google")
  //  //    val netflix = Amazon("Windows", "Microsoft","4.1","600","Welcome to Windows")
  //
  //      // save them to the mongodb database
  //      //saveDataIntoDB(apple)
  //      saveDataIntoLocationDB(google)
  //      saveDataIntoLocationDB(apple)
  //
  //    }


}

