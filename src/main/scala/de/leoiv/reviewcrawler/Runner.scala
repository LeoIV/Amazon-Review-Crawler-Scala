package de.leoiv.reviewcrawler

import java.io.{StringReader, FileWriter}

import au.com.bytecode.opencsv.CSVReader
import de.leoiv.reviewcrawler.entities.{Subcategory, Category}
import org.apache.spark.{SparkContext, SparkConf}

import org.apache.log4j.Logger
import org.apache.log4j.Level


/**
  * Created by hoevelmann on 14.10.2016.
  */
object Runner {

  def main(args: Array[String]): Unit = {
    // defining number of pages
    val numPages = 10;

    // Logger less verbose
    Logger.getLogger("org").setLevel(Level.WARN)
    Logger.getLogger("akka").setLevel(Level.WARN)

    // Defining Spark Context
    val conf = new SparkConf().setMaster("local").setAppName("ReviewCrawler")
    val sc = new SparkContext(conf)

    // Opening files
    val reviewInput = sc.textFile("reviews.csv")
    val reviewRdd = reviewInput.map { line =>
      // semicolon is the separator
      val reader = new CSVReader(new StringReader(line), ';');
      reader.readNext();
    }

    // opening file writers
    val reviewFileWriter = new FileWriter("reviews.csv", true)

    // Initial categories
    val categories = List(
      new Category("Buecher", "https://www.amazon.de/b%C3%BCcher-buch-lesen/b/ref=sd_allcat_bo?ie=UTF8&node=186606", numPages),
      new Category("Elektronik und Foto", "https://www.amazon.de/Elektronik-Foto/b/ref=sd_allcat_el?ie=UTF8&node=562066", numPages),
      new Category("Kueche und Haushalt", "https://www.amazon.de/k%C3%BCche-haushalt/b/ref=sd_allcat_allkhprod?ie=UTF8&node=3167641", numPages),
      new Category("Tiernahrung", "https://www.amazon.de/Tierbedarf-Tiernahrung/b/ref=sd_allcat_ps?ie=UTF8&node=340852031", numPages),
      new Category("Baumarkt", "https://www.amazon.de/baumarkt-werkzeug-heimwerken/b/ref=sd_allcat_diy?ie=UTF8&node=80084031", numPages),
      new Category("Garten", "https://www.amazon.de/garten-freizeit-grillen-gartenger%C3%A4te-garteneinrichtung/b/ref=sd_allcat_lg?ie=UTF8&node=10925031", numPages),
      new Category("Sportprodukte", "https://www.amazon.de/sport-freizeit-sportartikel/b/ref=sd_allcat_asf?ie=UTF8&node=16435051", numPages),
      new Category("Sportprodukte", "https://www.amazon.de/dvd-blu-ray-filme-3D-vhs-video/b/ref=sd_allcat_dvd_blu?ie=UTF8&node=284266", numPages)
    )

    for (subcategory <- categories.filter(_.subcategories.isSuccess).flatMap(_.subcategories.get)) {
      if (subcategory.products.isSuccess) {
        // ignore all products that already have a review in the text file
        for (product <- subcategory.products.get if reviewRdd.count() == 0 || reviewRdd.filter(rev => rev(1) == product.asin).count() == 0) {
          for (review <- product.reviews.get
               if product.reviews.isSuccess
               // if review is not already in text file
               if reviewRdd.count() == 0 || reviewRdd.filter(r => r(2) == review.amazonId).count() == 0) {
            reviewFileWriter.write(subcategory.name.replace(';', ',') + ";" + product.asin + ";" + review.amazonId + ";" + review.title.replace(';', ',') + ";" + review.reviewText.replace(';', ',') + ";" + review.rating + "\n")
          }
        }
      }
    }
    reviewFileWriter.close();
  }
}
