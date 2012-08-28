package org.dcache.xrootd4j

import scala.xml._
import collection.immutable.ListMap

object CmsSettingsParser {

    def parse(xmlString : String) : Map[String, String] = {
      val nodes = (XML.loadString(xmlString) \ "pfn-to-lfn")
        nodes.map(node => {
          val a = getAttribute(node)(_)
          val paths = buildPathMatchFor(Option.apply(node))(nodes)
          paths.map(path => {
            (a("protocol") + "://" + path._1 -> path._2)
          })
        }).foldLeft(ListMap[String, String]())(_ ++ _)
    }

    def getAttribute(node : Node)(attribute : String) =
      node.attribute(attribute) match {
        case Some(a) => a.text
        case _ => throw new IllegalStateException("attribute " + attribute + " is missing in node " + node)
    }

    def buildPathMatchFor(node : Option[Node])(fromNodes : NodeSeq) : List[(String, String)] = {
      node match {
        case Some(currentNode) => {
          val a = getAttribute(currentNode)(_)
          currentNode.attribute("chain") match {
            case Some(chainedProtocol) => (List[(String, String)]() /:
              fromNodes.filter(fromNode => fromNode.attribute("protocol") match {
                case Some(protocol) => protocol == chainedProtocol
                case None => false
              }).map(chainNode => {
                val subNodes = buildPathMatchFor(Option.apply(chainNode))(fromNodes)
                subNodes.map(chainedPath =>
                  (a("path-match").replace("(.*)", chainedPath._1), a("result").replace("$1", chainedPath._2)))
              }))(_ ++ _)
            case None => List((a("path-match"), a("result")))
          }
        }
        case None => Nil
      }
    }
}
