package de.leoiv.reviewcrawler

import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by hoevelmann on 14.10.2016.
  */
object Runner {

  def main(args: Array[String]) = {
    val db = Database.forConfig("remotedb")
  }

}
