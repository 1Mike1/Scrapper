package com.zomato.scala

import com.mongodb.casbah.Imports.{MongoDBObject, _}

import com.mongodb.casbah.MongoConnection

object Zomato_DB{
  private val SERVER = "localhost"
  private val PORT   = 27017
  private val DATABASE = "Food"
  private val COLLECTION = "Zomato_Details"

  val connection = MongoConnection(SERVER)
  val collection = connection(DATABASE)(COLLECTION)
  //println(collection)

  case class Zomato_Food(Name: String,Type:String,Rating:String)

  def buildMongoDbObject(zomatofood: Zomato_Food): MongoDBObject = {
    val builder = MongoDBObject.newBuilder;
    builder += "Name" -> zomatofood.Name;
    builder += "Type" -> zomatofood.Type;
    builder += "Rating" -> zomatofood.Rating;
    //println(builder.result)
    builder.result;
  }

  // our 'save' method
  def saveDataIntoDB(zomatofood: Zomato_Food) {
    val mongoObj = buildMongoDbObject(zomatofood)
    Zomato_DB.collection.save(mongoObj)
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

