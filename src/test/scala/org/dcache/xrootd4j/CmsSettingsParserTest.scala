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
class CmsSettingsParserTest extends FlatSpec {

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
    val map = CmsSettingsParser.parse(
      <storage-mapping>

        <!-- Specific for Load Test 07 input files-->
        <lfn-to-pfn protocol="srm"
                    path-match=".*/LoadTest07_DESY_(.*)_.*_.*"
                    result="srm://dcache-se-cms.desy.de:8443/srm/managerv1?SFN=/pnfs/desy.de/cms/tier2/store/phedex_monarctest/monarctest_DESY/MonarcTest_DESY_$1"/>
        <!-- For SRM2 -->
        <lfn-to-pfn protocol="srmv2"
                    path-match=".*/LoadTest07_DESY_(.*)_.*_.*"
                    result="srm://dcache-se-cms.desy.de:8443/srm/managerv2?SFN=/pnfs/desy.de/cms/tier2/store/phedex_monarctest/monarctest_DESY/MonarcTest_DESY_$1"/>

        <!-- DCMS MC production -->
        <lfn-to-pfn protocol="direct"
                    path-match="/+store/DCMS/(.*)"
                    result="/pnfs/desy.de/cms/analysis/dcms/$1"/>
        <lfn-to-pfn protocol="direct"
                    path-match="/+store/unmerged/DCMS/(.*)"
                    result="/pnfs/desy.de/cms/analysis/dcms/unmerged/$1"/>
        <pfn-to-lfn protocol="direct"
                    path-match="/pnfs/desy.de/cms/analysis/dcms/unmerged/(.*)"
                    result="/store/unmerged/DCMS/$1"/>
        <pfn-to-lfn protocol="direct"
                    path-match="/pnfs/desy.de/cms/analysis/dcms/(.*)"
                    result="/store/DCMS/$1"/>

        <!-- Special directories -->
        <lfn-to-pfn protocol="srm"
                    path-match="/+store/PhEDEx_LoadTest07/(.*)"
                    result="srm://dcache-se-cms.desy.de:8443/srm/managerv1?SFN=/pnfs/desy.de/cms/tier2/loadtest/$1"/>
        <pfn-to-lfn protocol="direct"
                    path-match="/pnfs/desy.de/cms/tier2/loadtest/(.*)"
                    result="/store/PhEDEx_LoadTest07/$1"/>
        <!-- For SRM2 -->
        <lfn-to-pfn protocol="srmv2"
                    path-match="/+store/PhEDEx_LoadTest07/(.*)"
                    result="srm://dcache-se-cms.desy.de:8443/srm/managerv2?SFN=/pnfs/desy.de/cms/tier2/loadtest/$1"/>

        <!-- unmerged/temp directories -->
        <lfn-to-pfn protocol="direct"
                    path-match="/+store/unmerged/(.*)"
                    result="/pnfs/desy.de/cms/tier2/unmerged/$1"/>
        <pfn-to-lfn protocol="direct"
                    path-match="/pnfs/desy.de/cms/tier2/unmerged/(.*)"
                    result="/store/unmerged/$1"/>
        <lfn-to-pfn protocol="direct"
                    path-match="/+store/temp/(.*)"
                    result="/pnfs/desy.de/cms/tier2/temp/$1"/>
        <pfn-to-lfn protocol="direct"
                    path-match="/pnfs/desy.de/cms/tier2/temp/(.*)"
                    result="/store/temp/$1"/>

        <!-- Open JobRobot sample via xrootd - dirty hack! -->
        <lfn-to-pfn protocol="dcap"
                    path-match="/+store/mc/JobRobot/(.*)"
                    result="root://dcache-cms-xrootd.desy.de/pnfs/desy.de/cms/tier2/store/mc/JobRobot/$1"/>
        <lfn-to-pfn protocol="dcap"
                    path-match="/+store/mc/CMSSW_4_2_3/RelValProdTTbar/GEN-SIM-RECO/MC_42_V12_JobRobot-v1/(.*)"
                    result="root://dcache-cms-xrootd.desy.de/pnfs/desy.de/cms/tier2/store/mc/CMSSW_4_2_3/RelValProdTTbar/GEN-SIM-RECO/MC_42_V12_JobRobot-v1/$1"/>

        <!-- Normal stuff here -->
        <!-- remote xrootd fallback via Bari redirector -->
        <lfn-to-pfn protocol="remote-xrootd"
                    path-match="/+(.*)"
                    result="root://xrootd.ba.infn.it//$1"/>
        <pfn-to-lfn protocol="remote-xrootd"
                    path-match="/(.*)"
                    result="/$1"/>
        <!-- End of redirector stuff -->
        <lfn-to-pfn protocol="direct"
                    path-match="/+(.*)"
                    result="/pnfs/desy.de/cms/tier2/$1"/>
        <lfn-to-pfn protocol="dcap" chain="direct"
                    path-match="(.*)"
                    result="dcap://dcache-cms-dcap.desy.de/$1"/>
        <lfn-to-pfn protocol="gsidcap" chain="direct"
                    path-match="(.*)"
                    result="gsidcap://dcache-cms-gsidcap.desy.de:22128/$1"/>
        <lfn-to-pfn protocol="srm" chain="direct"
                    path-match="(.*)"
                    result="srm://dcache-se-cms.desy.de:8443/srm/managerv1?SFN=$1"/>
        <pfn-to-lfn protocol="direct"
                    path-match="/pnfs/desy.de/cms/tier2/(.*)"
                    result="/$1"/>
        <pfn-to-lfn protocol="srm" chain="direct"
                    path-match=".*\?SFN=(.*)"
                    result="$1"/>
        <!-- For SRM2 -->
        <lfn-to-pfn protocol="srmv2" chain="direct"
                    path-match="(.*)"
                    result="srm://dcache-se-cms.desy.de:8443/srm/managerv2?SFN=$1"/>
        <pfn-to-lfn protocol="srmv2" chain="direct"
                    path-match=".*\?SFN=(.*)"
                    result="$1"/>
      </storage-mapping>)

    expect(32)(map.size)

    expect("srm://dcache-se-cms.desy.de:8443/srm/managerv2?SFN=/pnfs/desy.de/cms/tier2/$1") {
      map.get("srmv2:///+(.*)").get
    }
  }

  it should "understand the rules at CERN" in {
    val map = CmsSettingsParser.parse(
      <storage-mapping>
        <!-- Specific for LoadTest07  -->
        <lfn-to-pfn protocol="direct"
                    path-match=".*/LoadTest07_10GB_Source/CH_CERN_(.*).LTgenerated/T0_CH_CERN_Export/PhEDEx_Debug/LoadTest07_T0_CH_CERN_Export_10GB/.*"
                    result="/castor/cern.ch/cms/store/PhEDEx_LoadTest07_10GB/LoadTest07_CERN10GB_$1"/>
        <lfn-to-pfn protocol="direct"
                    path-match=".*/LoadTest07_10GB_Source/CH_CERN_(.*)"
                    result="/castor/cern.ch/cms/store/PhEDEx_LoadTest07_10GB/LoadTest07_CERN10GB_$1"/>
        <lfn-to-pfn protocol="direct"
                    path-match=".*/(LoadTest07_.*/T1_CH_CERN_Buffer/.*)"
                    result="/castor/cern.ch/cms/store/PhEDEx_LoadTest07/$1"/>
        <lfn-to-pfn protocol="direct"
                    path-match=".*/LoadTest07_.*_CERN_(.*)_.*_.*"
                    result="/castor/cern.ch/cms/store/PhEDEx_LoadTest07_4/LoadTest07_CERN_$1"/>
        <lfn-to-pfn protocol="direct"
                    path-match=".*/LoadTest07Source/T._CERN_(.*)"
                    result="/castor/cern.ch/cms/store/PhEDEx_LoadTest07_4/LoadTest07_CERN_$1"/>

        <!-- Map express data to t0express pool -->
        <lfn-to-pfn protocol="rfio"
                    path-match="/+store/express/(.*)"
                    result="root://eoscms//eos/cms/store/express/$1"/>
        <lfn-to-pfn protocol="stageout"
                    path-match="/+store/express/(.*)"
                    result="root://eoscms//eos/cms/store/express/$1"/>

        <!-- Map any LFN with /store/caf/user to a PFN in the cmscafuser pool or EOS -->
        <lfn-to-pfn protocol="rfio"
                    path-match="/+store/caf/user/(.*)"
                    result="root://eoscms//eos/cms/store/caf/user/$1"/>
        <lfn-to-pfn protocol="stageout"
                    path-match="/+store/caf/user/(.*)"
                    result="root://eoscms//eos/cms/store/caf/user/$1"/>

        <!-- Map any LFN with /store/group to a PFN on EOS -->
        <lfn-to-pfn protocol="rfio"
                    path-match="/+store/group/(.*)"
                    result="root://eoscms//eos/cms/store/group/$1"/>
        <lfn-to-pfn protocol="stageout"
                    path-match="/+store/group/(.*)"
                    result="root://eoscms//eos/cms/store/group/$1"/>

        <!-- Map any LFN with /store/cmst3/ to EOS -->
        <lfn-to-pfn protocol="rfio"
                    path-match="/+store/cmst3/(.*)"
                    result="root://eoscms//eos/cms/store/cmst3/$1"/>
        <lfn-to-pfn protocol="stageout"
                    path-match="/+store/cmst3/(.*)"
                    result="root://eoscms//eos/cms/store/cmst3/$1"/>

        <!-- Use /store/user/gowdy/rfio for testing RFIO -->
        <lfn-to-pfn protocol="rfio"
                    path-match="/+store/user/gowdy/rfio/(.*)"
                    result="rfio://castorcms//castor/cern.ch/cms/store/user/gowdy/rfio/$1?svcClass=default"/>
        <lfn-to-pfn protocol="stageout"
                    path-match="/+store/user/gowdy/rfio/(.*)"
                    result="rfio://castorcms//castor/cern.ch/cms/store/user/gowdy/rfio/$1?svcClass=default"/>

        <!-- Map any LFN with /store/user to a PFN on EOS -->
        <lfn-to-pfn protocol="stageout"
                    path-match="/+store/user/(.*)"
                    result="root://eoscms//eos/cms/store/user/$1"/>

        <!-- Map any LFN with /store/temp/user to a PFN on EOS -->
        <lfn-to-pfn protocol="stageout"
                    path-match="/+store/temp/user/(.*)"
                    result="root://eoscms//eos/cms/store/temp/user/$1"/>

        <!-- Map any LFN with /store/unmerged/ to a PFN on EOS -->
        <lfn-to-pfn protocol="stageout"
                    path-match="/+store/unmerged/(.*)"
                    result="root://eoscms//eos/cms/store/unmerged/$1"/>

        <!-- Map any LFN with /store/relval to a PFN on EOS -->
        <lfn-to-pfn protocol="stageout"
                    path-match="/+store/relval/(.*)"
                    result="root://eoscms//eos/cms/store/relval/$1"/>

        <!-- Map any LFN with /T0/hufnagel/recotest/store/t0temp to a PFN on EOS -->
        <lfn-to-pfn protocol="rfio"
                    path-match="/+T0/hufnagel/recotest/store/t0temp/(.*)"
                    result="root://eoscms//eos/cms/store/eos/user/hufnagel/t0temp/$1"/>
        <lfn-to-pfn protocol="direct"
                    path-match="/+T0/hufnagel/recotest/store/t0temp/(.*)"
                    result="/eos/cms/store/eos/user/hufnagel/t0temp/$1"/>
        <lfn-to-pfn protocol="stageout"
                    path-match="/+T0/hufnagel/recotest/store/t0temp/(.*)"
                    result="root://eoscms//eos/cms/store/eos/user/hufnagel/t0temp/$1"/>

        <!-- Map any LFN with /store/t0streamer to xrootd on t0streamer -->
        <lfn-to-pfn protocol="rfio"
                    path-match="/+store/t0streamer/(.*)"
                    result="root://castorcms//castor/cern.ch/cms/store/t0streamer/$1?svcClass=t0streamer&amp;stageHost=castorcms"/>
        <lfn-to-pfn protocol="stageout"
                    path-match="/+store/t0streamer/(.*)"
                    result="rfio://castorcms//castor/cern.ch/cms/store/t0streamer/$1?svcClass=t0streamer"/>

        <!-- Map any LFN with /store/t0temp to xrootd on EOS -->
        <lfn-to-pfn protocol="rfio"
                    path-match="/+store/t0temp/(.*)"
                    result="root://eoscms//eos/cms/store/t0temp/$1"/>
        <lfn-to-pfn protocol="direct"
                    path-match="/+store/t0temp/(.*)"
                    result="/eos/cms/store/t0temp/$1"/>
        <lfn-to-pfn protocol="stageout"
                    path-match="/+store/t0temp/(.*)"
                    result="root://eoscms//eos/cms/store/t0temp/$1"/>

        <!-- Specific for MC at CERN, only T2 -->
        <lfn-to-pfn protocol="stageout"
                    path-match="/+store/mc/(.*)"
                    result="root://eoscms//eos/cms/store/mc/$1"/>

        <!-- user/group data on EOS -->
        <lfn-to-pfn protocol="stageout"
                    path-match="/+store/eos/(.*)"
                    result="root://eoscms//eos/cms/store/eos/$1"/>

        <!-- LHE data on EOS -->
        <lfn-to-pfn protocol="stageout"
                    path-match="/+store/lhe/(.*)"
                    result="root://eoscms//eos/cms/store/lhe/$1"/>

        <!-- Tier0 test data on EOS -->

        <lfn-to-pfn path-match="/+store/backfill/3/(.*)" protocol="rfio"
                    result="root://eoscms//eos/cms/store/backfill/3/$1"/>
        <lfn-to-pfn path-match="/+store/backfill/3/(.*)" protocol="stageout"
                    result="root://eoscms//eos/cms/store/backfill/3/$1"/>

        <lfn-to-pfn path-match="/+store/backfill/2/t0temp/(.*)" protocol="rfio"
                    result="root://eoscms//eos/cms/store/backfill/2/t0temp/$1"/>
        <lfn-to-pfn path-match="/+store/backfill/2/t0temp/(.*)" protocol="stageout"
                    result="root://eoscms//eos/cms/store/backfill/2/t0temp/$1"/>

        <lfn-to-pfn path-match="/+store/backfill/2/express/(.*)" protocol="rfio"
                    result="root://eoscms//eos/cms/store/backfill/2/express/$1"/>
        <lfn-to-pfn path-match="/+store/backfill/2/express/(.*)" protocol="stageout"
                    result="root://eoscms//eos/cms/store/backfill/2/express/$1"/>

        <lfn-to-pfn path-match="/+store/backfill/(.*)" protocol="rfio"
                    result="root://castorcms//castor/cern.ch/cms/store/backfill/$1?svcClass=t0export&amp;stageHost=castorcms"/>
        <lfn-to-pfn path-match="/+store/backfill/(.*)" protocol="stageout"
                    result="rfio://castorcms//castor/cern.ch/cms/store/backfill/$1"/>

        <!-- Map pretend LFN /user to CASTOR User Space -->
        <lfn-to-pfn protocol="rfio"
                    path-match="/+user/(.*)"
                    result="root://castorcms//castor/cern.ch/user/$1?svcClass=default"/>
        <lfn-to-pfn protocol="stageout"
                    path-match="/+user/(.*)"
                    result="root://castorcms//castor/cern.ch/user/$1?svcClass=default"/>


        <!-- Map any LFN with /store/ to xrootd on EOS, fall back to default -->
        <lfn-to-pfn protocol="rfio"
                    path-match="/+store/(.*)"
                    result="root://eoscms//eos/cms/store/$1?svcClass=default"/>

        <!-- Below is the default rule -->
        <lfn-to-pfn protocol="direct"
                    path-match="/+(.*)"
                    result="/castor/cern.ch/cms/$1"/>
        <lfn-to-pfn protocol="stageout" chain="direct"
                    path-match="(.*)"
                    result="$1"/>
        <lfn-to-pfn protocol="rfio" chain="direct"
                    path-match="(.*)"
                    result="rfio:$1"/>
        <lfn-to-pfn protocol="srmv2"
                    path-match="/+store/user/(.*)"
                    result="srm://srm-eoscms.cern.ch:8443/srm/v2/server?SFN=/eos/cms/store/user/$1"/>
        <lfn-to-pfn protocol="srmv2" chain="direct" destination-match="T1_CH_CERN_.*" is-custodial="y"
                    path-match="(.*)"
                    result="srm://srm-cms.cern.ch:8443/srm/managerv2?SFN=$1"
                    space-token="CMS_DEFAULT"/>
        <lfn-to-pfn protocol="srmv2" chain="direct" destination-match="T1_CH_CERN_.*" is-custodial="n"
                    path-match="(.*)"
                    result="srm://srm-cms.cern.ch:8443/srm/managerv2?SFN=$1"
                    space-token="CMS_DEFAULT"/>
        <lfn-to-pfn protocol="srmv2" chain="direct" destination-match="T2_CH_CAF"
                    path-match="(.*)"
                    result="srm://srm-cms.cern.ch:8443/srm/managerv2?SFN=$1"
                    space-token="CMS_CAF"/>
        <lfn-to-pfn protocol="srmv2" chain="direct" destination-match="T2_CH_CERN"
                    path-match="(.*)"
                    result="srm://srm-eoscms.cern.ch:8443/srm/v2/server?SFN=$1"/>
        <lfn-to-pfn protocol="srmv2" chain="direct"
                    path-match="(.*)"
                    result="srm://srm-cms.cern.ch:8443/srm/managerv2?SFN=$1"/>

        <pfn-to-lfn protocol="direct"
                    path-match="/+castor/cern\.ch/cms/(.*)"
                    result="/$1"/>
        <pfn-to-lfn protocol="rfio" chain="direct"
                    path-match="^rfio:/+(.*)"
                    result="/$1"/>
        <pfn-to-lfn protocol="srmv2" chain="direct"
                    path-match=".*\?SFN=(.*)"
                    result="$1"/>

      </storage-mapping>)

    expect(63)(map.size)

    expect("srm://srm-cms.cern.ch:8443/srm/managerv2?SFN=/castor/cern.ch/cms/store/PhEDEx_LoadTest07_4/LoadTest07_CERN_$1") {
      map.get("srmv2://.*/LoadTest07_.*_CERN_(.*)_.*_.*").get
    }
  }

}


