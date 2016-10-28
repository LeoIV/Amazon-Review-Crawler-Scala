import java.io.IOException

import de.leoiv.reviewcrawler.entities.Category
import org.scalatest.{Matchers, FlatSpec, Suite}

/**
  * Created by hoevelmann on 28.10.2016.
  */
class CategorySpec extends FlatSpec with Matchers {
  "A category \"Bücher\" " should "have a list of subcategories" in {
    val category = new Category("Buecher", "https://www.amazon.de/b%C3%BCcher-buch-lesen/b/ref=sd_allcat_bo?ie=UTF8&node=186606", 2)
    assert(category.subcategories.get.length > 0)
    assert(category.subcategories.get.head.name.length > 0)
  }

  "A category \"Elektronik & Foto\" " should "have a list of subcategories" in {
    val category = new Category("Elektronik und Foto", "https://www.amazon.de/Elektronik-Foto/b/ref=sd_allcat_el?ie=UTF8&node=562066", 2)
    assert(category.subcategories.get.length > 0)
    assert(category.subcategories.get.head.name.length > 0)
  }

  "A category \"Küche & Haushalt\" " should "have a list of subcategories" in {
    val category = new Category("Kueche und Haushalt", "https://www.amazon.de/k%C3%BCche-haushalt/b/ref=sd_allcat_allkhprod?ie=UTF8&node=3167641", 2)
    assert(category.subcategories.get.length > 0)
    assert(category.subcategories.get.head.name.length > 0)
  }

  "A category \"Tiernahrung\" " should "have a list of subcategories" in {
    val category = new Category("Tiernahrung", "https://www.amazon.de/Tierbedarf-Tiernahrung/b/ref=sd_allcat_ps?ie=UTF8&node=340852031", 2)
    assert(category.subcategories.get.length > 0)
    assert(category.subcategories.get.head.name.length > 0)
  }

  "A category \"Baumarkt\" " should "have a list of subcategories" in {
    val category = new Category("Baumarkt", "https://www.amazon.de/baumarkt-werkzeug-heimwerken/b/ref=sd_allcat_diy?ie=UTF8&node=80084031", 2)
    assert(category.subcategories.get.length > 0)
    assert(category.subcategories.get.head.name.length > 0)
  }

  "A category \"Garten\" " should "have a list of subcategories" in {
    val category = new Category("Garten", "https://www.amazon.de/garten-freizeit-grillen-gartenger%C3%A4te-garteneinrichtung/b/ref=sd_allcat_lg?ie=UTF8&node=10925031", 2)
    assert(category.subcategories.get.length > 0)
    assert(category.subcategories.get.head.name.length > 0)
  }

  "A category \"Sportprodukte\" " should "have a list of subcategories" in {
    val category = new Category("Sportprodukte", "https://www.amazon.de/sport-freizeit-sportartikel/b/ref=sd_allcat_asf?ie=UTF8&node=16435051", 2)
    assert(category.subcategories.get.length > 0)
    assert(category.subcategories.get.head.name.length > 0)
  }

  "A category \"DVDs und BluRays\" " should "have a list of subcategories" in {
    val category = new Category("Sportprodukte", "https://www.amazon.de/dvd-blu-ray-filme-3D-vhs-video/b/ref=sd_allcat_dvd_blu?ie=UTF8&node=284266", 2)
    assert(category.subcategories.get.length > 0)
    assert(category.subcategories.get.head.name.length > 0)
  }

  "A category" should "be undefined if the url is wrong" in {
    val category = new Category("Buecher", "https://www.amazon.de/fantasieUrl", 2)
    assert(category.subcategories.isFailure);
  }
}
