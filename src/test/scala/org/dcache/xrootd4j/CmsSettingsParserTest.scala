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
import org.scalatest._
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CmsSettingsParserTest extends FlatSpec
  with DesyRulesFixture
  with CernRulesFixture {

  "The storage parser" should "throw an NPE" in {
    intercept[NullPointerException] {
      CmsSettingsParser.parse(null)
    }
  }

  it should "return an empty map for empty xml" in {
    val map = CmsSettingsParser.parse(<storage/>)

    assert(map.isEmpty)
  }

  it should "return an empty map for no pnf-to-lfn entries" in {
    val map = CmsSettingsParser.parse(
      <storage-mapping>
        <pfn-to-lfn protocol="bla" path-match="(.+)" result="$1"/>
      </storage-mapping>)

    assert(map.isEmpty)
  }

  it should "combine chained rules" in {
    val map = CmsSettingsParser.parse(
      <storage-mapping>
        <lfn-to-pfn protocol="direct" path-match="/+(.*)" result="/castor/cern.ch/cms/$1"/>
        <lfn-to-pfn protocol="srm" chain="direct" path-match="(.*)" result="srm://srm.cern.ch/srm/managerv1?SFN=$1"/>
      </storage-mapping>)


    assertResult("srm://srm.cern.ch/srm/managerv1?SFN=/castor/cern.ch/cms/$1") {
      map.get("srm:///+(.*)").get
    }
  }

  it should "add multiple rules for multiple chains" in {
    val map = CmsSettingsParser.parse(
    <storage-mapping>
      <lfn-to-pfn protocol="direct" path-match="/+store/(.*)" result="/castor/cern.ch/cms/store/$1"/>
      <lfn-to-pfn protocol="direct" path-match="/+(.*)" result="/castor/cern.ch/cms/$1"/>
      <lfn-to-pfn protocol="srm" chain="direct" path-match="(.*)" result="srm://srm.cern.ch/srm/managerv1?SFN=$1"/>
    </storage-mapping>)

    assertResult("srm://srm.cern.ch/srm/managerv1?SFN=/castor/cern.ch/cms/store/$1") {
      map.get("srm:///+store/(.*)").get
    }
    assertResult("srm://srm.cern.ch/srm/managerv1?SFN=/castor/cern.ch/cms/$1") {
      map.get("srm:///+(.*)").get
    }
  }

  {
    val map = CmsSettingsParser.parse(DesyStorageXmlFixture)

    it should "understand rules at DESY" in {
      assertResult(32)(map.size)
    }

    "A map from DESY rules" should "contain mapping for srm LoadTest07" in {
      assertResult("srm://dcache-se-cms.desy.de:8443/srm/managerv1?SFN=/pnfs/desy.de/cms/tier2/store/phedex_monarctest/monarctest_DESY/MonarcTest_DESY_$1") {
        map.get("srm://.*/LoadTest07_DESY_(.*)_.*_.*").get
      }
    }

    it should "contain mapping for srmv2 LoadTest07" in {
      assertResult("srm://dcache-se-cms.desy.de:8443/srm/managerv2?SFN=/pnfs/desy.de/cms/tier2/store/phedex_monarctest/monarctest_DESY/MonarcTest_DESY_$1") {
        map.get("srmv2://.*/LoadTest07_DESY_(.*)_.*_.*").get
      }
    }

    it should "contain mapping for srm PhEDEx LoadTest07" in {
      assertResult("srm://dcache-se-cms.desy.de:8443/srm/managerv1?SFN=/pnfs/desy.de/cms/tier2/loadtest/$1") {
        map.get("srm:///+store/PhEDEx_LoadTest07/(.*)").get
      }
    }

    it should "contain mapping for srmv2 PhEDEx LoadTest07" in {
      assertResult("srm://dcache-se-cms.desy.de:8443/srm/managerv2?SFN=/pnfs/desy.de/cms/tier2/loadtest/$1") {
        map.get("srmv2:///+store/PhEDEx_LoadTest07/(.*)").get
      }
    }

    it should "contain mapping for dcap JobRobot" in {
      assertResult("root://dcache-cms-xrootd.desy.de/pnfs/desy.de/cms/tier2/store/mc/JobRobot/$1") {
        map.get("dcap:///+store/mc/JobRobot/(.*)").get
      }
    }

    it should "contain mapping for remote-xrootd" in {
      assertResult("root://xrootd.ba.infn.it//$1") {
        map.get("remote-xrootd:///+(.*)").get
      }
    }

    it should "contain chained mapping for dcap" in {
      assertResult("dcap://dcache-cms-dcap.desy.de//pnfs/desy.de/cms/analysis/dcms/$1") {
        map.get("dcap:///+store/DCMS/(.*)").get
      }
    }

    it should "contain chained mapping for gsidcap" in {
      assertResult("gsidcap://dcache-cms-gsidcap.desy.de:22128//pnfs/desy.de/cms/analysis/dcms/unmerged/$1") {
        map.get("gsidcap:///+store/unmerged/DCMS/(.*)").get
      }
    }

    it should "contain chained mapping for srm" in {
      assertResult("srm://dcache-se-cms.desy.de:8443/srm/managerv1?SFN=/pnfs/desy.de/cms/tier2/$1") {
        map.get("srm:///+(.*)").get
      }
    }
  }

  "The storage parser" should "understand the rules at CERN" in {
    val map = CmsSettingsParser.parse(CernStorageXmlFixture)

    assertResult(63)(map.size)

    assertResult("srm://srm-cms.cern.ch:8443/srm/managerv2?SFN=/castor/cern.ch/cms/store/PhEDEx_LoadTest07_4/LoadTest07_CERN_$1") {
      map.get("srmv2://.*/LoadTest07_.*_CERN_(.*)_.*_.*").get
    }
  }
}

