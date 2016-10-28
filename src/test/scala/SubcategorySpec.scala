import de.leoiv.reviewcrawler.entities.{Subcategory, Category}
import org.scalatest.{Matchers, FlatSpec}

/**
  * Created by hoevelmann on 28.10.2016.
  */
class SubcategorySpec extends FlatSpec with Matchers{
  "Subcategory" should "have products if the URL is right" in {
    val subcategory = new Subcategory("Sachbuecher", "https://www.amazon.de/fachb%C3%BCcher-fachbuch/b/ref=amb_link_GOY-UPPTN2aqFsoJaBtp0g_18?ie=UTF8&node=288100&pf_rd_m=A3JWKAKR8XB7XF&pf_rd_s=merchandised-search-left-2&pf_rd_r=GPERS863AGC1DKXZ69XJ&pf_rd_r=GPERS863AGC1DKXZ69XJ&pf_rd_t=101&pf_rd_p=badb29e3-fc36-4fc2-959e-f9a50a89176b&pf_rd_p=badb29e3-fc36-4fc2-959e-f9a50a89176b&pf_rd_i=186606", 2)
    assert(subcategory.products.isSuccess)
    val products = subcategory.products.get
    assert(products.size > 0)
    assert(products.head.name.length > 0)
  }
  it should "connect but fail if the url is wrong" in {
    val subcategory1 = new Subcategory("Thrille und So", "https://www.amazon.de/reineFantasie", 2)
    assert(subcategory1.products.isFailure)
  }
  it should "connect but have an empty list for certain products such as Kindle" in {
    val subcategory2 = new Subcategory("Kindle","https://www.amazon.de/gp/product/B0186FESVC/ref=amb_link_ClWbitzUOgyc60-gg9F_Ow_7?pf_rd_m=A3JWKAKR8XB7XF&pf_rd_s=merchandised-search-left-4&pf_rd_r=GPERS863AGC1DKXZ69XJ&pf_rd_r=GPERS863AGC1DKXZ69XJ&pf_rd_t=101&pf_rd_p=c0f3aae2-ce53-4bdd-95ec-5d817012f201&pf_rd_p=c0f3aae2-ce53-4bdd-95ec-5d817012f201&pf_rd_i=186606",2)
    assert(subcategory2.products.isSuccess)
    assert(subcategory2.products.get.size == 0)
  }
}
