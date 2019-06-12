package com.swiggy.scala

import com.mongodb.casbah.Imports.{MongoDBObject, _}
import com.mongodb.casbah.MongoConnection

object SwiggyDB{
  private val SERVER = "localhost"
  private val PORT   = 27017
  private val DATABASE = "Food"
  private val COLLECTION = "HotelDetails"

  val connection = MongoConnection(SERVER)
  val collection = connection(DATABASE)(COLLECTION)
  //println(collection)

  case class Swiggy_Food(Name: String,Type:String,Rating:String)

  def buildMongoDbObject(swiggyfood: Swiggy_Food): MongoDBObject = {
    val builder = MongoDBObject.newBuilder;
    builder += "Name" -> swiggyfood.Name;
    builder += "Type" -> swiggyfood.Type;
    builder += "Rating" -> swiggyfood.Rating;
    //println(builder.result)
    builder.result;
  }

  // our 'save' method
  def saveDataIntoDB(swiggyfood: Swiggy_Food) {
    val mongoObj = buildMongoDbObject(swiggyfood)
    SwiggyDB.collection.save(mongoObj)
  }

  //  def main(args:Array[String]): Unit ={
  //    //val apple = Amazon("ios", "Apple","4.5","600","Welcome to ios")
  ////    val google = Amazon("Android","Google","4.0","650","Welcome To Android")
  ////    val netflix = Amazon("Windows", "Microsoft","4.1","600","Welcome to Windows")
  //
  //    // save them to the mongodb database
  //    //saveDataIntoDB(apple)
  ////    saveDataIntoDB(google)
  ////    saveDataIntoDB(netflix)
  //
  //  }


}
