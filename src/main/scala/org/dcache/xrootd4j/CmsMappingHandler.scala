/**
 *  This file is part of xrootd4j-cms-plugin.
 *
 *  xrootd4j-cms-plugin is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  xrootd4j-cms-plugin is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 *  of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with xrootd4j-cms-plugin.  If not, see
 *  <http://www.gnu.org/licenses/.>
 *
 */
package org.dcache.xrootd4j

import java.net.InetSocketAddress
import javax.security.auth.Subject

import org.dcache.xrootd.protocol.XrootdProtocol.FilePerm
import org.dcache.xrootd.plugins.AuthorizationHandler
import collection.immutable.ListMap

class CmsMappingHandler(mappings : Map[String, String]) extends AuthorizationHandler
{
  override def authorize(subject : Subject,
                         localAddress : InetSocketAddress,
                         remoteAddress : InetSocketAddress,
                         path : String,
                         opaque : java.util.Map[String, String],
                         request : Int,
                         mode : FilePerm) : String =
  {
    val applicableRules = mappings.find(rule => rule._1.r.findFirstIn(path).isDefined)

    applicableRules match {
      case Some((pattern, replacement)) => pattern.r.replaceFirstIn(path, replacement)
      case None => path
    }
  }
}
