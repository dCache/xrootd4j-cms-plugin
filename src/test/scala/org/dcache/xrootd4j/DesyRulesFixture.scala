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

trait DesyRulesFixture {

  val DesyStorageXmlFixture =
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
    </storage-mapping>
}
