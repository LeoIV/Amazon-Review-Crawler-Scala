name := "amazonReviewCrawler"

version := "1.0"

scalaVersion := "2.11.7"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
mainClass in assembly := Some("de.leoiv.reviewcrawler.Runner")
test in assembly := {}

libraryDependencies += "org.jsoup" % "jsoup" % "1.10.1"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.0.1" % "provided"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"
