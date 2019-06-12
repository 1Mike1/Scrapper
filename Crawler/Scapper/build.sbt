name := "Scapper"

version := "0.1"

scalaVersion := "2.11.0"

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.6.0"
libraryDependencies += "org.mongodb" %% "casbah" % "3.1.1" pomOnly()
libraryDependencies += "org.jsoup" % "jsoup" % "1.11.3"