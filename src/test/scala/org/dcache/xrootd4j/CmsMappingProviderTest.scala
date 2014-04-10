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

import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.junit.runner.RunWith
import java.util.Properties

@RunWith(classOf[JUnitRunner])
class CmsMappingProviderTest extends FlatSpec {

  def provider = new CmsMappingProvider

  "A CMS mapping provider" should "return a CmsMappingFactory if called with the factories name" in {
    val properties = new Properties()
    properties.put(CmsMappingFactory.FilenameProperty, "settings.xml")
    properties.put(CmsMappingFactory.ProtocolProperty, "root")
    val factory = provider.createFactory(CmsMappingFactory.Name, properties)

    assert(factory != null)
  }

  it should "return null for non matching names" in {
    assertResult(null) {
      provider.createFactory("other", new Properties())
    }
  }

}
