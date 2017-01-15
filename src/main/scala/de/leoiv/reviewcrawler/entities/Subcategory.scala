package de.leoiv.reviewcrawler.entities

import de.leoiv.reviewcrawler.services.ConnectorService
import org.jsoup.nodes.Element

import scala.collection.JavaConverters._
import scala.util.Try

/**
  * Created by hoevelmann on 14.10.2016.
  * leonard.hoevelmann@posteo.de
  *
  */
class Subcategory(val name: String, val url: String, val pages: Int) {

  lazy val products: Try[List[Product]] = Try(fetchProducts(List(), url, 1))

  private def fetchProducts(outerProductAcc: List[Product], url: String, currentPage: Int): List[Product] = {

    // Connect with amazon
    val doc = ConnectorService(url, 1000)

    val linkContainer = doc getElementsByClass ("s-result-item")
    //val linksWrapper = linkContainer.get(0).getElementsByClass("s-access-detail-page")
    val linkContainerList = List() ++ linkContainer.asScala

    def collectProducts(productAcc: List[Product], elements: List[Element]): List[Product] = elements match {
      case Nil => productAcc
      case head :: tail => {
        val currentLinkContainer: Element = head
        val name = currentLinkContainer.getElementsByClass("s-access-detail-page").get(0).attr("title")
        val asin = currentLinkContainer.attr("data-asin")
        val prod = new Product(asin, name, pages)
        collectProducts(prod :: productAcc, tail)
      }
    }

    val currentReviewList = collectProducts(outerProductAcc, linkContainerList)

    // if element "next page" is not clickable or the current page is larger than the speci
    if (currentPage >= pages || linkContainer.isEmpty) {
      currentReviewList
    }
    else {
      fetchProducts(currentReviewList, "https://www.amazon.de" + doc.getElementById("pagnNextString").parent().attr("href"), currentPage + 1)
    }
  }

  override def toString(): String = "Subcategory(" + name + "," + url + "," + pages + ", lazy val products)"
}
