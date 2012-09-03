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
class CmsSettingsParserTest extends FlatSpec
  with DesyRulesFixture
  with CernRulesFixture {

  "The storage parser" should "throw an NPE" in {
    intercept[NullPointerException] {
      CmsSettingsParser.parse(null)
    }
  }

  it should "return an empty map for empty xml" in
    {
      val map = CmsSettingsParser.parse(<storage/>)

      assert(map.isEmpty)
    }

  it should "return an empty map for no pnf-to-lfn entries" in
    {
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


    expect("srm://srm.cern.ch/srm/managerv1?SFN=/castor/cern.ch/cms/$1") {
      map.get("srm:///+(.*)").get
    }
  }

  it should "understand rules at DESY" in {
    val map = CmsSettingsParser.parse(DesyStorageXmlFixture)

    expect(32)(map.size)

    expect("srm://dcache-se-cms.desy.de:8443/srm/managerv2?SFN=/pnfs/desy.de/cms/tier2/$1") {
      map.get("srmv2:///+(.*)").get
    }
  }

  it should "understand the rules at CERN" in {
    val map = CmsSettingsParser.parse(CernStorageXmlFixture)

    expect(63)(map.size)

    expect("srm://srm-cms.cern.ch:8443/srm/managerv2?SFN=/castor/cern.ch/cms/store/PhEDEx_LoadTest07_4/LoadTest07_CERN_$1") {
      map.get("srmv2://.*/LoadTest07_.*_CERN_(.*)_.*_.*").get
    }
  }

}


