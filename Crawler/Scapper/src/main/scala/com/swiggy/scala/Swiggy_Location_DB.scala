package com.swiggy.scala

import com.mongodb.casbah.Imports.{MongoDBObject, _}
import com.mongodb.casbah.MongoConnection

object Swiggy_Location_DB{
  private val SERVER = "localhost"
  private val PORT   = 27017
  private val DATABASE = "Food"
  private val COLLECTION = "Location"

  val connection = MongoConnection(SERVER)
  val collection = connection(DATABASE)(COLLECTION)
  //println(collection)

  case class Swiggy_Location(Location: String,Url:String)

  def buildMongoDbObject(swiggy: Swiggy_Location): MongoDBObject = {
    val builder = MongoDBObject.newBuilder;
    builder += "Location" -> swiggy.Location;
    builder += "Url" -> swiggy.Url;
//    builder += "Rating" -> amazon.Rating;
//    builder += "Price" -> amazon.Price;
//    builder += "Specification" -> amazon.Specs;
    println(builder.result)
    builder.result;
  }

  // our 'save' method
  def saveDataIntoLocationDB(swiggy_loc: Swiggy_Location) {
    val mongoObj = buildMongoDbObject(swiggy_loc)
    Swiggy_Location_DB.collection.save(mongoObj)
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
