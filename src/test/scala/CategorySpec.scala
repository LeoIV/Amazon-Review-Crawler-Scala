import de.leoiv.reviewcrawler.entities.Category
import org.scalatest.{Matchers, FlatSpec, Suite}

/**
  * Created by hoevelmann on 28.10.2016.
  */
class CategorySpec extends FlatSpec with Matchers {
  "A category" should "have a list of subcategories" in {
    val category = new Category("Buecher", "https://www.amazon.de/b%C3%BCcher-buch-lesen/b/ref=sd_allcat_bo?ie=UTF8&node=186606", 2)
    category.subcategories.size should be > 0
    category.subcategories.head.name.length should be > 0
  }
}
