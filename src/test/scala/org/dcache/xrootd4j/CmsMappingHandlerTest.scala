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

import org.scalatest._
import junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class CmsMappingHandlerTest extends FlatSpec
  with DesyRulesFixture {

  def fixture = new CmsMappingHandler(CmsSettingsParser.parse(DesyStorageXmlFixture))
  val handler = fixture

  "The CMSMappingHandler" should "leave path with unmatched protocol unchanged" in {
    expect("ftp://store/blubb") {
      handler.authorize(null, null, null,
        "ftp://store/blubb",
        null, 0, null)
    }
  }

  it should "leave an unmatched path unchanged" in {
    expect("srm://unmatched/blubb") {
      handler.authorize(null, null, null,
        "srm://unmatched/blubb",
        null, 0, null)
    }
  }

  it should "map a matching path" in {
    expect("root://xrootd.ba.infn.it//somepath/wurstbrot") {
      handler.authorize(null, null, null,
        "remote-xrootd:///somepath/wurstbrot",
        null, 0, null)
    }
  }

  it should "map a matching chained rule" in {
    expect("srm://dcache-se-cms.desy.de:8443/srm/managerv2?SFN=/pnfs/desy.de/cms/tier2/schwupp/ti/du/temp/wurstbrot") {
      handler.authorize(null, null, null,
        "srmv2:///schwupp/ti/du/temp/wurstbrot",
        null, 0, null)
    }
  }

}
