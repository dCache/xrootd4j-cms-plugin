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
    final val Name = "xrootd4j.cms.settings"

    final val AlternativeNames = Set(Name)

    final val FilenameProperty = "xrootd.cms.storage.path"

    def hasName(name : String) = (AlternativeNames contains name)
}

class CmsMappingFactory(properties : Properties) extends AuthorizationFactory
{
    val filename = properties.getProperty(CmsMappingFactory.FilenameProperty)
    require(Option(filename).nonEmpty)

    override def getName = CmsMappingFactory.Name

    override def getDescription = "CMS Name Mapping Plugin"

    override def createHandler = {
        val xml = XML.load(new URL(filename))
        val mappings = CmsSettingsParser.parse(xml)
        new CmsMappingHandler(mappings)
    }
}
