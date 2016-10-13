package reviewcrawler.entities

/**
  * Created by hoevelmann on 13.10.2016.
  */
@SerialVersionUID(42832475L)
class Review(val amazonId: String, val rating: Byte, val title: String, val reviewText: String) extends Serializable {
  override def toString(): String = "Review(" + amazonId + "," + rating + "," + title + "," + reviewText + ")"
}
