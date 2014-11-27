/**
 * Copyright (C) 2011-2014 dCache.org <support@dcache.org>
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

trait CernRulesFixture {

  val CernStorageXmlFixture =
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

    </storage-mapping>

}
