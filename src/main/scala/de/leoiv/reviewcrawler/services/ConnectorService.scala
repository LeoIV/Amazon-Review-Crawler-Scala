package de.leoiv.reviewcrawler.services

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
  * Created by hoevelmann on 14.10.2016.
  * leonard.hoevelmann@posteo.de
  *
  */
class ConnectorService {

  // acting as a firefox
  def document(url: String): Document = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
    .timeout(10000).ignoreHttpErrors(true).followRedirects(true).get();

}

object ConnectorService {
  def document(url: String) = new ConnectorService().document(url)
}
