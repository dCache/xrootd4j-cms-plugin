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

import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatestplus.junit.JUnitRunner
import java.util.Properties

@RunWith(classOf[JUnitRunner])
class CmsMappingFactoryTest extends FlatSpec {

  "The CmsMappingFactory" should "have name cms-tfc" in {
    assert(CmsMappingFactory.hasName("cms-tfc"))
  }

  it should "throw an NPE for null properties" in {
    intercept[NullPointerException] {
      new CmsMappingFactory(null)
    }
  }

  it should "throw an IllegalArgumentException for empty properties" in {
    intercept[IllegalArgumentException] {
      new CmsMappingFactory(new Properties)
    }
  }

  it should "throw an IllegalArgumentException if the protocol property is missing" in {
    intercept[IllegalArgumentException] {
      val properties = new Properties
      properties.put(CmsMappingFactory.FilenameProperty, "http://cmssw.cvs.cern.ch/cgi-bin/cmssw.cgi/COMP/SITECONF/T2_DE_DESY/PhEDEx/storage.xml?revision=1.10&content-type=text%2Fplain")
      new CmsMappingFactory(new Properties)
    }
  }

  it should "throw an IllegalArgumentException if the filename property is missing" in {
    intercept[IllegalArgumentException] {
      val properties = new Properties
      properties.put(CmsMappingFactory.ProtocolProperty, "root")
      new CmsMappingFactory(new Properties)
    }
  }

  it should "create a handler based on the file at an URL" in {
    val properties = new Properties()
    properties.put(CmsMappingFactory.FilenameProperty, "http://cmssw.cvs.cern.ch/cgi-bin/cmssw.cgi/COMP/SITECONF/T2_DE_DESY/PhEDEx/storage.xml?revision=1.10&content-type=text%2Fplain")
    properties.put(CmsMappingFactory.ProtocolProperty, "root")
    new CmsMappingFactory(properties)
  }
}
