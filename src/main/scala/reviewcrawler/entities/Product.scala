package reviewcrawler.entities

/**
  * Created by hoevelmann on 13.10.2016.
  */
@SerialVersionUID(3566474325L)
class Product(var amazonId: String, var name: String, var reviews: List[Review]) extends Serializable {
  override def toString(): String = "Product(" + amazonId + "," + name + "," + reviews + ")"
}
