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

  def response(url: String): Response = {

    def res = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36").timeout(10000).ignoreHttpErrors(true).followRedirects(true).execute()
    res.statusCode() match {
      case 200 => res
      case _ => {
        println("Connection failed. Trying again...")
        Thread.sleep(1000)
        res
      }
    }
  }

  // acting as a firefox
  def document(url: String): Document = {
    println("Connecting to " + url)
    def res = response(url)
    def doc = res.parse()
    doc
  }

}

object ConnectorService {
  def document(url: String) = new ConnectorService().document(url)
}
