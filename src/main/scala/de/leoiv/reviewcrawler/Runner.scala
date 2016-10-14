package de.leoiv.reviewcrawler

import java.util.Properties

import de.leoiv.reviewcrawler.database_entities.Review
import de.leoiv.reviewcrawler.entities.Category
import slick.driver.MySQLDriver.api._


/**
  * Created by hoevelmann on 14.10.2016.
  */
object Runner {

  def main(args: Array[String]): Unit = {

    val cat = Category("Buecher", "https://www.amazon.de/b%C3%BCcher-buch-lesen/b/ref=sd_allcat_bo?ie=UTF8&node=186606", 2)

    val iter = for {
      subcategory <- cat.subcategories
      if subcategory.products.isSuccess;
      product <- subcategory.products.get
      if product.reviews.isSuccess;
      review <- product.reviews.get
    } yield (0, review.amazonId, review.rating, review.title, review.reviewText, product.asin, product.name)

    for (i <- iter)
      println(i)


    val reviews = TableQuery[Review]
    val db = Database.forURL("jdbc:mysql://localhost/projektarbeit", "root", "", new Properties(), "com.mysql.jdbc.Driver")
    try {
      val setup = DBIO.seq(
        //reviews.schema.create,
        reviews ++= iter.toIterable)
      //  reviews +=(23, "test", 4, "test", "test", "test", "test"))
      db.run(setup.transactionally)
    }
    catch {
      case e: Exception => e.printStackTrace()
    }
    finally db.close()
  }
}
