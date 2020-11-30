/**
 * Copyright (C) 2011-2020 dCache.org <support@dcache.org>
 *
 * This file is part of xrootd4j-cms-plugin.
 *
 * xrootd4j-cms-plugin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * xrootd4j-cms-plugin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with xrootd4j-cms-plugin.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.dcache.xrootd4j

import scala.xml._
import collection.immutable.ListMap

object CmsSettingsParser {

    def parse(rootNode : Node) : Map[String, String] = {
      val nodes = (rootNode \ "lfn-to-pfn")
      ListMap.apply(nodes.flatMap(node => {
        val paths = buildPathMatchFor(node)(nodes)
        paths.map { case (pathMatch, result) => (getAttribute(node)("protocol") + "://" + pathMatch -> result) }
      }):_*)
    }

    def getAttribute(node : Node)(attribute : String) =
      node.attribute(attribute) match {
        case Some(a) => a.text
        case _ => throw new IllegalStateException("attribute " + attribute + " is missing in node " + node)
    }

    def buildPathMatchFor(node : Node)(fromNodes : NodeSeq) : List[(String, String)] = {

      def nodesFilteredBy(protocol : String)(fromNodes : NodeSeq) =
        fromNodes.filter(_.attribute("protocol").exists(_.text == protocol))

      def toMatchResultTupleListFrom(node : Node)(withAttributes : String => String)(fromNodes : NodeSeq) = {
        val subMatchResultTuples = buildPathMatchFor(node)(fromNodes)
        subMatchResultTuples.map { case (pathMatch, result) =>
          (withAttributes("path-match").replace("(.*)", pathMatch),
           withAttributes("result")    .replace(  "$1", result)) }
      }

      val withAttributes = getAttribute(node)(_)
      node.attribute("chain") match {
        case Some(chainedProtocol) => nodesFilteredBy(chainedProtocol.text)(fromNodes)
          .flatMap(chainNode => toMatchResultTupleListFrom(chainNode)(withAttributes)(fromNodes))
          .toList
        case None => List((withAttributes("path-match"), withAttributes("result")))
      }
    }
}
