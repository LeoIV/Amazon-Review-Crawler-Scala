package de.leoiv.reviewcrawler.entities

/**
  * Created by hoevelmann on 13.10.2016.
  */
@SerialVersionUID(6534485L)
class Category(val id: Int, val name: String, val products: List[Product]) extends Serializable {
  override def toString(): String = "Category(" + id + "," + name + "," + products + ")"
}
