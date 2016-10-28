import de.leoiv.reviewcrawler.entities.{Subcategory, Category}
import org.scalatest.{Matchers, FlatSpec}

/**
  * Created by hoevelmann on 28.10.2016.
  */
class SubcategorySpec extends FlatSpec with Matchers{
  "Subcategory Thriller" should "have products" in {
    val subcategory = new Subcategory("Thrille und So", "https://www.amazon.de/thriller-spannende-b%C3%83%C2%BCcher-krimis/b/ref=amb_link_GOY-UPPTN2aqFsoJaBtp0g_10?ie=UTF8&node=287480&pf_rd_m=A3JWKAKR8XB7XF&pf_rd_s=merchandised-search-left-2&pf_rd_r=GPERS863AGC1DKXZ69XJ&pf_rd_r=GPERS863AGC1DKXZ69XJ&pf_rd_t=101&pf_rd_p=badb29e3-fc36-4fc2-959e-f9a50a89176b&pf_rd_p=badb29e3-fc36-4fc2-959e-f9a50a89176b&pf_rd_i=186606", 2)
    subcategory.products.isSuccess should be true
    val products = subcategory.products.get
    products.size should be > 0
    products.head.name.length should be > 0
  }
}
