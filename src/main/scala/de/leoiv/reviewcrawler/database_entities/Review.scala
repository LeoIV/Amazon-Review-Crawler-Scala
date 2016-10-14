package de.leoiv.reviewcrawler.database_entities

import slick.driver.MySQLDriver.api._

/**
  * Created by hoevelmann on 14.10.2016.
  */
class Review(tag: Tag) extends Table[(Int, String, Byte, String, String, Int)](tag, "REVIEW") {
  def id = column[Int]("REVIEW_ID", O.PrimaryKey, O.AutoInc)

  def amazonId = column[String]("REVIEW_AMAZON_ID")

  def rating = column[Byte]("REVIEW_RATING")

  def reviewTitle = column[String]("REVIEW_TITLE")

  def reviewText = column[String]("REVIEW_TEXT")

  def productId = column[Int]("REVIEW_PRODUCT_ID")

  def * = (id, amazonId, rating, reviewTitle, reviewText, productId)

  def product = foreignKey("PRODUCT", productId, TableQuery[Product])

  val reviews = TableQuery[Review]
}
