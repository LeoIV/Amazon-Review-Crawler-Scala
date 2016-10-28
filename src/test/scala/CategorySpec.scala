import java.io.IOException

import de.leoiv.reviewcrawler.entities.Category
import org.scalatest.{Matchers, FlatSpec, Suite}

/**
  * Created by hoevelmann on 28.10.2016.
  */
class CategorySpec extends FlatSpec with Matchers {
  "A category\"Bücher\" " should "have a list of subcategories if the URL is right" in {
    val category = new Category("Buecher", "https://www.amazon.de/b%C3%BCcher-buch-lesen/b/ref=sd_allcat_bo?ie=UTF8&node=186606", 2)
    assert(category.subcategories.size > 0)
    assert(category.subcategories.head.name.length > 0)
  }

  "A category\"Kamera & Foto\" " should "have a list of subcategories if the URL is right" in {
    val category = new Category("Buecher", "https://www.amazon.de/Kamera-Foto-Digitalkameras-Spiegelreflexkameras-Camcorder/b/ref=sd_allcat_p?ie=UTF8&node=571860", 2)
    assert(category.subcategories.size > 0)
    assert(category.subcategories.head.name.length > 0)
  }

  "A category\"Smartphones & Zubehör\" " should "have a list of subcategories if the URL is right" in {
    val category = new Category("Buecher", "https://www.amazon.de/Handys-Smartphones-Handyvertr%C3%A4ge/b/ref=sd_allcat_wi?ie=UTF8&node=571954", 2)
    assert(category.subcategories.size > 0)
    assert(category.subcategories.head.name.length > 0)
  }

  "A category" should "throw an exception on startup if the URL is wrong" in {
    val category = new Category("Buecher", "https://www.amazon.de/fantasieUrl", 2)
    a[IllegalArgumentException] should be thrownBy {
      val subcategories = category.subcategories;
    }
  }
}
