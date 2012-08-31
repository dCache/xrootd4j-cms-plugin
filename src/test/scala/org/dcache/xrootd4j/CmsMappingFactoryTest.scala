/**
 * Copyright (C) 2011,2012 dCache.org <support@dcache.org>
 *
 * This file is part of xrootd4j.
 *
 * xrootd4j is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * xrootd4j is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with xrootd4j.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.dcache.xrootd4j

import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.junit.runner.RunWith
import java.util.Properties

@RunWith(classOf[JUnitRunner])
class CmsMappingFactoryTest extends FlatSpec {

  "The CmsMappingFactory" should "have name xrootd4j.cms.settings" in {
    assert(CmsMappingFactory.hasName("xrootd4j.cms.settings"))
  }

  it should "throw an NPE for null properties" in {
    intercept[NullPointerException] {
      new CmsMappingFactory(null)
    }
  }

  it should "throw an NPE if the filename property is missing" in {
    intercept[NullPointerException] {
      new CmsMappingFactory(new Properties)
    }
  }
}
