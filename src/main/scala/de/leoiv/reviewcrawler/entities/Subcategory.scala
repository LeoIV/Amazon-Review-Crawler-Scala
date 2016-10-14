package de.leoiv.reviewcrawler.entities

import de.leoiv.reviewcrawler.services.ConnectorService
import org.jsoup.nodes.Element

import scala.collection.JavaConverters._
import scala.util.Try

/**
  * Created by hoevelmann on 14.10.2016.
  */
class Subcategory(val name: String, val url: String, val pages: Int) {

  println("subcategory created ("+name+")")

  lazy val products: Try[List[Product]] = Try(fetchProducts)

  private def fetchProducts: List[Product] = {
    val doc = ConnectorService.document(url)
    val linkContainer = doc getElementsByClass ("s-result-item")
    //val linksWrapper = linkContainer.get(0).getElementsByClass("s-access-detail-page")
    val linkContainerList = List() ++ linkContainer.asScala

    def collectProducts(productAcc: List[Product], elements: List[Element]): List[Product] = elements match {
      case Nil => productAcc
      case h :: t => {
        val currentLinkContainer: Element = elements.head
        val name = currentLinkContainer.getElementsByClass("s-access-detail-page").get(0).attr("title")
        val asin = currentLinkContainer.attr("data-asin")
        collectProducts(new Product(asin, name, pages) :: productAcc, elements.tail)
      }
    }
    collectProducts(List(), linkContainerList)
  }

  override def toString(): String = "Subcategory(" + name + "," + url + "," + pages + ", lazy val products)"
}
