package de.leoiv.reviewcrawler

import de.leoiv.reviewcrawler.entities.Review
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
  def allReviews(pages: Int): List[Review] = {
    def collectReviews(reviewAcc: List[Review], currentStar: Int): List[Review] = {
      currentStar match {
        case 6 => reviewAcc
        case _ => reviewsWithRating(currentStar, pages) ::: collectReviews(reviewAcc, currentStar + 1)
      }
    }
    collectReviews(List(), 1)
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
          val url = "http://www.amazon.de/product-reviews/" + productId + "/ref=cm_cr_arp_d_hist_" + stars + "?filterByStar=" + stars + "_star&pageNumber=" + currentPage
          collectReviews(reviewAcc ::: seachReviewsOnPage(url), currentPage + 1, lastPage, stars)
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
  private def seachReviewsOnPage(url: String): List[Review] = {
    def getRating(classNames: Set[String]): Byte = {
      val starString = classNames.filter(_.indexOf("a-star-") >= 0).head
      starString.substring(starString.length - 1, starString.length) toByte
    }

    println("fetching from " + url)
    val document = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
      .timeout(10000).ignoreHttpErrors(true).followRedirects(true).get();
    val reviewContainers: Elements = document getElementById ("cm_cr-review_list") getElementsByClass ("review");
    val reviews = for {element: Element <- reviewContainers.asScala
                       rating: Set[String] = Set() ++ JavaConversions.asScalaSet(element.getElementsByClass("review-rating").get(0).classNames())
                       amazonId: String = element.id()
                       reviewTitle: String = element.getElementsByClass("review-title").get(0).text()
                       reviewText: String = element.getElementsByClass("review-text").text()
    } yield new Review(amazonId, getRating(Set() ++ rating), reviewTitle, reviewText)
    val reviewList = List() ++ reviews
    reviewList
  }
}