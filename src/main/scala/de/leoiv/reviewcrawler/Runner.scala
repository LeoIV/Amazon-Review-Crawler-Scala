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

    val conf = new SparkConf().setMaster("local").setAppName("ReviewCrawler")
    val sc = new SparkContext(conf)

    val input = sc.textFile("reviews.csv")
    val result = input.map { line =>
      // semicolon is the separator
      val reader = new CSVReader(new StringReader(line), ';');
      reader.readNext();
    }

    // Initial category
    val category = new Category("Buecher", "https://www.amazon.de/b%C3%BCcher-buch-lesen/b/ref=sd_allcat_bo?ie=UTF8&node=186606", 10)

    val subcategories = category.subcategories

    // ignore all products that already have a review in the text file
    val products = subcategories.map(_.products).filter(_.isSuccess).flatMap(_.get).filter(prod0 => result.count() == 0 || result.filter(prod1 => prod1(1) == prod0.asin).count() == 0)

    val fw = new FileWriter("reviews.csv", true)

    for (product <- products) {
      for (review <- product.reviews.get
           if product.reviews.isSuccess) {
        fw.write(category.name + ";" + product.asin + ";" + review.amazonId + ";" + review.title.replace(';', ',') + ";" + review.reviewText.replace(';', ',') + ";" + review.rating + "\n")
      }
    }
    fw.close();
  }
}
