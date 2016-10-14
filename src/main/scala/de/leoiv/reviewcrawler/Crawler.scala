package de.leoiv.reviewcrawler

import de.leoiv.reviewcrawler.entities.Review
import de.leoiv.reviewcrawler.services.ConnectorService
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import scala.collection.JavaConversions
import scala.collection.JavaConverters._


/**
  * Product Crawler
  *
  * Created by hoevelmann on 13.10.2016.
  */
class Crawler(val productId: String) {

  /**
    * Collects Reviews for all stars (1-5)
    *
    * @param pages the method will fetch reviews from pages 1 - (pages-1)
    * @return
    */

}