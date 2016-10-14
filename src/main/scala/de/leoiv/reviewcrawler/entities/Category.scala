package de.leoiv.reviewcrawler.entities

import de.leoiv.reviewcrawler.services.ConnectorService
import org.jsoup.nodes.Element

import scala.collection.JavaConversions
import scala.collection.JavaConverters._


/**
  * Created by hoevelmann on 13.10.2016.
  */
@SerialVersionUID(6534485L)
class Category(val name: String, val url: String, val pages: Int) {

  println("category created ("+name+")")

  lazy val subcategories: List[Subcategory] = fetchSubkategorien

  private def fetchSubkategorien: List[Subcategory] = {
    val doc = ConnectorService.document(url)
    val linkContainer = doc getElementsByClass ("left_nav")
    val links = List() ++ linkContainer.get(0).getElementsByTag("a").asScala

    def collectSubcategories(categoryAcc: List[Subcategory], elements: List[Element]): List[Subcategory] = elements match {
      case Nil => categoryAcc
      case h :: t => {
        val currentLink: Element = elements.head
        collectSubcategories(new Subcategory(currentLink.text(), "https://www.amazon.de"+currentLink.attr("href"), pages) :: categoryAcc, elements.tail)
      }
    }

    collectSubcategories(List(), links)
  }

  override def toString(): String = "Category(" + name + "," + url + "," + pages + ", lazy val subcategories)"
}
