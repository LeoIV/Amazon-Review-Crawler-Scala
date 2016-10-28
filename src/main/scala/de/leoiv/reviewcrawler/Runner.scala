package de.leoiv.reviewcrawler

import java.io.{StringReader, FileWriter, PrintWriter}

import au.com.bytecode.opencsv.CSVReader
import de.leoiv.reviewcrawler.entities.Category
import org.apache.spark.{SparkContext, SparkConf}


/**
  * Created by hoevelmann on 14.10.2016.
  */
object Runner {

  def main(args: Array[String]): Unit = {
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

    // Initial category
    val category = new Category("Buecher", "https://www.amazon.de/b%C3%BCcher-buch-lesen/b/ref=sd_allcat_bo?ie=UTF8&node=186606", 10)

    // Select only subcategories, that arent already saved in one of the CSVs
    val subcategories = category.subcategories

    // ignore all products that already have a review in the text file
    val products = subcategories.map(_.products).filter(_.isSuccess).flatMap(_.get).filter(prod => reviewRdd.count() == 0 || reviewRdd.filter(rev => rev(1) == prod.asin).count() == 0)

    for (product <- products) {
      for (review <- product.reviews.get
           if product.reviews.isSuccess) {
        reviewFileWriter.write(category.name.replace(';', ',') + ";" + product.asin + ";" + review.amazonId + ";" + review.title.replace(';', ',') + ";" + review.reviewText.replace(';', ',') + ";" + review.rating + "\n")
      }
    }
    reviewFileWriter.close();
  }
}
