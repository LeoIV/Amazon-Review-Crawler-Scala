package de.leoiv.reviewcrawler.entities

import de.leoiv.reviewcrawler.services.ConnectorService
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import scala.collection.{JavaConversions, mutable}
import scala.collection.JavaConverters._
import scala.util.Try

/**
  * Created by hoevelmann on 13.10.2016.
  */
class Product(var asin: String, var name: String, val pages: Int) extends Serializable {

  lazy val reviews: Try[List[Review]] = Try(allReviews(pages))

  def allReviews(pages: Int): List[Review] = {
    def collectReviews(reviewAcc: List[Review], currentStar: Int): List[Review] = {
      currentStar match {
        case 6 => reviewAcc
        case _ => reviewsWithRating(currentStar, pages) ::: collectReviews(reviewAcc, currentStar + 1)
      }
    }

    val reviewList = collectReviews(List(), 1)
    if (reviewList.length > 0) println("collected " + reviewList.length + " reviews for product " + name)
    reviewList
  }

  /**
    * Collects reviews for one specific star rating
    *
    * @param stars reviews with this star rating will be fetched
    * @param pages the method will fetch reviews from pages 1 - (pages-1)
    * @return
    */
    def reviewsWithRating(stars: Int, pages: Int): List[Review] = {
    def starsAsString(star: Int): String = star match {
    case 1 => "one"
    case 2 => "two"
      case 3 => "three"
      case 4 => "four"
      case 5 => "five"
      case _ => throw new IllegalArgumentException("star not in range [1,5]")
    }

    def collectReviews(reviewAcc: List[Review], currentPage: Int, lastPage: Int, stars: Int): List[Review] = {
      currentPage match {
        case `lastPage` => reviewAcc
        case _ => {
          val url = "http://www.amazon.de/product-reviews/" + asin + "/ref=cm_cr_arp_d_hist_" + stars + "?filterByStar=" + starsAsString(stars) + "_star&pageNumber=" + currentPage
          val reviewsOnPage = searchReviewsOnPage(url)
          collectReviews(reviewsOnPage.getOrElse(List()) ::: reviewAcc, currentPage + 1, lastPage, stars)
        }
      }
    }

    collectReviews(List(), 1, pages, stars)
  }

  /**
    * Looks for all reviews on page
    *
    * @param url the url from which is fetched
    * @return
    */
  private def searchReviewsOnPage(url: String): Try[List[Review]] = {
    def getRating(classNames: Set[String]): Byte = {
      val starString = classNames.filter(_.indexOf("a-star-") >= 0).head
      starString.substring(starString.length - 1, starString.length) toByte
    }

    Try {
      val document = ConnectorService(url, 10000)
      val reviewContainers: Elements = document getElementById ("cm_cr-review_list") getElementsByClass ("review");
      val reviews: mutable.Seq[Review] = for {element: Element <- reviewContainers.asScala
                                              rating: Set[String] = Set() ++ JavaConversions.asScalaSet(element.getElementsByClass("review-rating").get(0).classNames())
                                              amazonId: String = element.id()
                                              reviewTitle: String = element.getElementsByClass("review-title").get(0).text()
                                              reviewText: String = element.getElementsByClass("review-text").text()
      } yield new Review(amazonId, getRating(Set() ++ rating), reviewTitle, reviewText)
      reviews.toList
    }
  }

  override def toString(): String = "Product(" + asin + "," + name + "," + "," + pages + ", lazy val reviews)"
}
