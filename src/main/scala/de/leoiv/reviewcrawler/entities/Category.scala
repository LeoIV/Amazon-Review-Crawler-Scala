package de.leoiv.reviewcrawler.entities

import de.leoiv.reviewcrawler.services.ConnectorService
import org.jsoup.nodes.Element

import scala.collection.JavaConverters._


/**
  * A category is something like "books" or "dvds", something that has subcategorys
  *
  * Created by hoevelmann on 13.10.2016.
  * leonard.hoevelmann@posteo.de
  */
class Category(val name: String, val url: String, val pages: Int) {

  println("category created (" + name + ")")

  lazy val subcategories: List[Subcategory] = fetchSubkategorien

  private def fetchSubkategorien: List[Subcategory] = {
    // Connect with amazon
    val doc = ConnectorService.document(url)
    // Get all elements of left sidebar
    val linkContainer = doc getElementsByClass ("left_nav")
    // Then, get all links in there (as a Scala sequence)
    val links = List() ++ linkContainer.get(0).getElementsByTag("a").asScala

    def collectSubcategories(categoryAcc: List[Subcategory], elements: List[Element]): List[Subcategory] = elements match {
      case Nil => categoryAcc
      case h :: t => {
        val currentLink: Element = elements.head
        collectSubcategories(new Subcategory(currentLink.text(), "https://www.amazon.de" + currentLink.attr("href"), pages) :: categoryAcc, elements.tail)
      }
    }
    collectSubcategories(List(), links)
  }

  override def toString(): String = "Category(" + name + "," + url + "," + pages + ", lazy val subcategories)"
}

object Category{
  def apply(name: String, url: String, pages: Int): Category = new Category(name, url, pages)
}