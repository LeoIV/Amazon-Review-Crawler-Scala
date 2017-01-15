package de.leoiv.reviewcrawler

import java.io.{FileWriter, StringReader}

import de.leoiv.reviewcrawler.entities.{Category, Subcategory}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.log4j.Logger
import org.apache.log4j.Level


/**
  * Created by hoevelmann on 14.10.2016.
  */
object Runner {

  def main(args: Array[String]): Unit = {
    // defining number of pages
    val numPages = 20;

    // Logger less verbose
    Logger.getLogger("org").setLevel(Level.WARN)
    Logger.getLogger("akka").setLevel(Level.WARN)

    // Defining Spark Context
    val conf = new SparkConf().setMaster("local").setAppName("ReviewCrawler")
    val sc = new SparkContext(conf)

    // Opening files
    val reviewInput = sc.textFile("reviews.csv")
    val reviewRdd = reviewInput.map(_.split(";"))

    // opening file writers
    val reviewFileWriter = new FileWriter("reviews.csv", true)

    // Initial categories
    val categories = List(
      // new Category("Buecher", "https://www.amazon.de/b%C3%BCcher-buch-lesen/b/ref=sd_allcat_bo?ie=UTF8&node=186606", numPages),
      new Category("Kamera und Foto", "https://www.amazon.de/Kamera-Foto-Digitalkameras-Spiegelreflexkameras-Camcorder/b/ref=sd_allcat_p?ie=UTF8&node=571860", numPages),
      //new Category("Kueche und Haushalt", "https://www.amazon.de/k%C3%BCche-haushalt/b/ref=sd_allcat_allkhprod?ie=UTF8&node=3167641", numPages),
      // new Category("Tiernahrung", "https://www.amazon.de/Tierbedarf-Tiernahrung/b/ref=sd_allcat_ps?ie=UTF8&node=340852031", numPages),
      // new Category("Baumarkt", "https://www.amazon.de/baumarkt-werkzeug-heimwerken/b/ref=sd_allcat_diy?ie=UTF8&node=80084031", numPages),
       new Category("Garten", "https://www.amazon.de/garten-freizeit-grillen-gartenger%C3%A4te-garteneinrichtung/b/ref=sd_allcat_lg?ie=UTF8&node=10925031", numPages),
      new Category("Sportprodukte", "https://www.amazon.de/sport-freizeit-sportartikel/b/ref=sd_allcat_asf?ie=UTF8&node=16435051", numPages)
      //new Category("DVDs und Blu Rays", "https://www.amazon.de/dvd-blu-ray-filme-3D-vhs-video/b/ref=sd_allcat_dvd_blu?ie=UTF8&node=284266", numPages)
    )

    for (category <- categories.filter(_.subcategories.isSuccess)) {
      for (subcategory <- category.subcategories.get) {
        if (subcategory.products.isSuccess) {
          // ignore all products that already have a review in the text file
          for (product <- subcategory.products.get if reviewRdd.count() == 0 || reviewRdd.filter(rev => rev(1) == product.asin).count() == 0) {
            for (review <- product.reviews.get
                 if product.reviews.isSuccess
                 // if review is not already in text file
                 if reviewRdd.count() == 0 || reviewRdd.filter(r => r(2) == review.amazonId).count() == 0) {
              reviewFileWriter.write(category.name.replace(';', ',') + ";" + product.asin + ";" + review.amazonId + ";" + review.title.replace(';', ',') + ";" + review.reviewText.replace(';', ',') + ";" + review.rating + "\n")
            }
          }
        }
      }
    }
    reviewFileWriter.close();

    // printing statistics

    println("\nRatings per detailed class (5 stars first)")
    val reviewsDetailed = sc.textFile("reviews.csv").map(_.split(";")).filter(_.length == 6).map(r => {
      var rev = r(0)
      var arr = new Array[Int](5)
      arr(4 - (r(5).toInt - 1)) = 1
      (rev, arr)
    }).reduceByKey((r1, r2) => {
      val arr = new Array[Int](5)
      for (i <- 0 until 5)
        arr(i) = r1(i) + r2(i)
      arr
    })

    reviewsDetailed.collect().foreach(r => println(r._1 + ": " + r._2.mkString(",")))

    println("\nRatings per polarity (positive first)")
    reviewsDetailed.map(r => {
      val key = r._1
      val arr = new Array[Int](2)
      for (i <- 0 until 5)
        if (i < 2)
          arr(0) += r._2(i)
        else if (i >= 2)
          arr(1) += r._2(i)
      (key, arr)
    }).collect().foreach(r => println(r._1 + ": " + r._2.mkString(",")))
  }
}
