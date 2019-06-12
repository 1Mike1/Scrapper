package com.amazon.scala

import com.mongodb.casbah.Imports.{MongoDBObject, _}
import com.mongodb.casbah.MongoConnection

object MongoFactory {
  private val SERVER = "localhost"
  private val PORT   = 27017
  private val DATABASE = "E-Commerce_Websites"
  private val COLLECTION = "Amazon"

  val connection = MongoConnection(SERVER)
  val collection = connection(DATABASE)(COLLECTION)
  //println(collection)

  case class Amazon (Name: String,Brand:String,Rating:String,Price: String,Specs:String)

  def buildMongoDbObject(amazon: Amazon): MongoDBObject = {
    val builder = MongoDBObject.newBuilder;
    builder += "Name" -> amazon.Name;
    builder += "Brand" -> amazon.Brand;
    builder += "Rating" -> amazon.Rating;
    builder += "Price" -> amazon.Price;
    builder += "Specification" -> amazon.Specs;
    //println(builder.result)
    builder.result;
  }

  // our 'save' method
  def saveDataIntoDB(amazon: Amazon) {
    val mongoObj = buildMongoDbObject(amazon)
    MongoFactory.collection.save(mongoObj)
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