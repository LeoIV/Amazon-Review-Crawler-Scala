package de.leoiv.reviewcrawler

import de.leoiv.reviewcrawler.database.ReviewMapping
import de.leoiv.reviewcrawler.entities._
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by hoevelmann on 14.10.2016.
  */
object Runner {

  val cat = new Category("Buecher", "https://www.amazon.de/b%C3%BCcher-buch-lesen/b/ref=sd_allcat_bo?ie=UTF8&node=186606", 20)
  val iter = for {
    subcategory <- cat.subcategories
    productTries <- subcategory.products
    product <- productTries
    reviews <- product.reviews
    review <- reviews
  } yield (review.amazonId, review.amazonId, review.rating, review.title, review.reviewText, product.asin, product.name)


  def main(args: Array[String]) = {
    val reviews = TableQuery[ReviewMapping]
    val db = Database.forConfig("remotedb")

    val setup = DBIO.seq(
      reviews.schema.create,
      reviews ++= Seq(iter))
  }

}
