package de.leoiv.reviewcrawler.database_entities

import slick.driver.MySQLDriver.api._

/**
  * Created by hoevelmann on 14.10.2016.
  */
class Product(tag: Tag) extends Table[(Int, String, String, Int, Int)](tag, "PRODUCT") {
  def id = column[Int]("PRODUCT_ID", O.PrimaryKey, O.AutoInc)

  def asin = column[String]("PRODUCT_ASIN")

  def name = column[String]("PRODUCT_NAME")

  def pages = column[Int]("PRODUCT_PAGES")

  def subcategoryId = column[Int]("PRODUCT_SUBCATEGORY_ID")

  def * = (id, asin, name, pages, subcategoryId)

  def subcategory = foreignKey("SUBCATEGORY", subcategoryId, TableQuery[Subcategory])
}
