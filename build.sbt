name := "amazonReviewCrawler"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "org.jsoup" % "jsoup" % "1.10.1"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.0.1"
libraryDependencies += "net.sf.opencsv" % "opencsv" % "2.3"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"