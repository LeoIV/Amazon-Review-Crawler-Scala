package de.leoiv.reviewcrawler.entities

/**
  * A review has an ASIN, a rating, a title and a review text
  *
  * Created by hoevelmann on 13.10.2016.
  * leonard.hoevelmann@posteo.de
  *
  */
class Review(val amazonId: String, val rating: Byte, val title: String, val reviewText: String) extends Serializable {
  override def toString(): String = "Review(" + amazonId + "," + rating + "," + title + "," + reviewText + ")"
}
