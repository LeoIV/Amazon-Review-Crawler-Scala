package de.leoiv.reviewcrawler.database

import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by hoevelmann on 14.10.2016.
  */
class ReviewMapping(tag: Tag) extends Table[(Int, String, Byte, String, String, String, String)](tag, "REVIEW") {
  def id = column[Int]("REVIEW_ID",O.PrimaryKey, O.AutoInc)
  def amazonId = column[String]("REVIEW_AMAZON_ID")
  def rating = column[Byte]("REVIEW_RATING")
  def reviewTitle = column[Sring]("REVIEW_TITLE")
  def reviewText = column[String]("REVIEW_TEXT")
  def productAsin = column[String]("REVIEW_PRODUCT_ASIN")
  def productName = column[String]("REVIEW_PRODUCT_NAME")

  def * = (id, amazonId, rating, reviewTitle, reviewText, productAsin, productName)

  val reviews = TableQuery[ReviewMapping]
}
