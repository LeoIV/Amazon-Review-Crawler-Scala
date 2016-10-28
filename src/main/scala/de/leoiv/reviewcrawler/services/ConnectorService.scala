package de.leoiv.reviewcrawler.services

import org.jsoup.Connection.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
  * Created by hoevelmann on 14.10.2016.
  * leonard.hoevelmann@posteo.de
  *
  */
class ConnectorService {

  def response(url: String, timeout: Int): Response = {
    def res = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36").timeout(timeout).ignoreHttpErrors(true).followRedirects(true).execute()
    res.statusCode() match {
      case 200 => res
      case 404 => throw new IllegalArgumentException;
      case _ => {
        println("Connection failed. Trying again...")
        Thread.sleep(1000)
        response(url, timeout)
      }
    }
  }

  // acting as a firefox
  def document(url: String, timeout: Int): Document = {
    println("Connecting to " + url)
    def res = response(url, timeout)
    def doc = res.parse()
    doc
  }

}

object ConnectorService {
  def apply(url: String, timeout: Int) = new ConnectorService().document(url, timeout)
}
