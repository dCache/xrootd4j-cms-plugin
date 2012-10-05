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

import org.dcache.xrootd.plugins.AuthorizationFactory
import java.util.Properties
import xml.XML
import java.net.URL

object CmsMappingFactory
{
    final val Name = "cms-tfc"

    final val AlternativeNames = Set(Name)

    final val FilenameProperty = "xrootd.cms.tfc.path"
    final val ProtocolProperty = "xrootd.cms.tfc.protocol"

    def hasName(name : String) = (AlternativeNames contains name)
}

class CmsMappingFactory(properties : Properties) extends AuthorizationFactory with Logging
{
    val filename = properties.getProperty(CmsMappingFactory.FilenameProperty)
    require(Option(filename).nonEmpty)

    val protocol = properties.getProperty(CmsMappingFactory.ProtocolProperty)
    require(Option(protocol).nonEmpty)

    override def getName = CmsMappingFactory.Name

    override def getDescription = "CMS Name Mapping Plugin"

    override def createHandler = {
      logger.debug("creating mappings from '" + filename + "'")
      val xml = filename match {
        case "^[^:]+://" => XML.load(filename)
        case _ => XML.loadFile(filename)
      }
      logger.trace("with content: \n" + xml)

      val mappings = CmsSettingsParser.parse(xml)
      logger.debug("mappings map: " + mappings)

      new CmsMappingHandler(mappings, protocol)
    }
}
