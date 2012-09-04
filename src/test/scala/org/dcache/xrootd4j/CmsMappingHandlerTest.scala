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

import org.scalatest._
import junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class CmsMappingHandlerTest extends FlatSpec
  with DesyRulesFixture {

  val map = CmsSettingsParser.parse(DesyStorageXmlFixture)
  val handler = new CmsMappingHandler(map)
  val mappedFilenameFor = handler.authorize(null, null, null, _ : String, null, 0, null)

  "The CMSMappingHandler with DESY rules" should "leave path with unmatched protocol unchanged" in {
    expect("ftp://store/blubb") {
      mappedFilenameFor("ftp://store/blubb")
    }
  }

  it should "leave an unmatched path unchanged" in {
    expect("srm://unmatched/blubb") {
      mappedFilenameFor("srm://unmatched/blubb")
    }
  }

  it should "map a matching path" in {
    expect("root://xrootd.ba.infn.it//somepath/wurstbrot") {
      mappedFilenameFor("remote-xrootd:///somepath/wurstbrot")
    }
  }

  it should "map a matching chained rule" in {
    expect("srm://dcache-se-cms.desy.de:8443/srm/managerv2?SFN=/pnfs/desy.de/cms/tier2/schwupp/ti/du/temp/wurstbrot") {
      mappedFilenameFor("srmv2:///schwupp/ti/du/temp/wurstbrot")
    }
  }

  it should "use the rules in the right order" in {
    expect("dcap://dcache-cms-dcap.desy.de//pnfs/desy.de/cms/analysis/dcms/unmerged/wurstbrot") {
      mappedFilenameFor("dcap:////store/unmerged/DCMS/wurstbrot")
    }
  }
}
