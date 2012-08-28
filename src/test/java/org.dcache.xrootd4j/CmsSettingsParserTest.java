package org.dcache.xrootd4j;

import org.junit.*;
import org.xml.sax.SAXParseException;

public class CmsSettingsParserTest {

    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullString() {
        CmsSettingsParser.parse(null);
    }

    @Test(expected = SAXParseException.class)
    public void shouldThrowIllegalArgumentExceptionForEmptyString() {
        CmsSettingsParser.parse("");
    }

    @Test
    public void shouldReturnEmptyMapForEmptySettings() {
        Assert.assertTrue(CmsSettingsParser.parse("<storage-mapping/>").isEmpty());
    }

    @Test
    public void shouldReturnEmptyMapForNoPnfToLfnEntries() {
        Assert.assertTrue(CmsSettingsParser.parse(
                "<storage-mapping>" +
                        "<lfn-to-pfn protocol=\"bla\" path-match=\"(.+)\" result=\"$1\"/>" +
                "</storage-mapping>").isEmpty());
    }

    @Test
    public void shouldReturnMapContainingRules() {
        scala.collection.immutable.Map<String, String> map = CmsSettingsParser.parse(
                        "<storage-mapping>\n" +
                            "<lfn-to-pfn protocol=\"dcap\"\n" +
                                "path-match=\"/+LoadTest/(.*)\" result=\"/pnfs/fs/usr/cms/WAX/9/LoadTest/$1\"/>\n" +
                            "<lfn-to-pfn protocol=\"dcap\"\n" +
                                "path-match=\"/+store/(.*)\" result=\"dcap://cmsdca.fnal.gov:24136/pnfs/fnal.gov/usr/cms/WAX/$1\"/>\n" +
                            "<lfn-to-pfn protocol=\"direct\"\n" +
                                "path-match=\"/+LoadTest/(.*)\" result=\"/pnfs/fs/usr/cms/WAX/9/LoadTest/$1\"/>\n" +
                            "<lfn-to-pfn protocol=\"direct\"\n" +
                                "path-match=\"/+store/(.*)\" result=\"dcap://cmsdca.fnal.gov:24136/pnfs/fnal.gov/usr/cms/WAX/$1\"/>\n" +
                            "<lfn-to-pfn protocol=\"srm\"\n" +
                                "path-match=\"/+LoadTest/(.*)\" result=\"srm://cmssrm.fnal.gov:8443/srm/managerv1?SFN=/9/$1\"/>\n" +
                            "<lfn-to-pfn protocol=\"srm\"\n" +
                                "path-match=\"/+store/(.*)\" result=\"srm://cmssrm.fnal.gov:8443/srm/managerv1?SFN=/11/store/$1\"/>\n" +
                            "<pfn-to-lfn protocol=\"direct\"\n" +
                                "path-match=\"/+pnfs/fs/usr/cms/WAX/11/store/(.*)\" result=\"/store/$1\"/>\n" +
                             "<pfn-to-lfn protocol=\"srm\"\n" +
                                "path-match=\".*\\?SFN=/9/LoadTest/(.*)\" result=\"/LoadTest/$1\"/>\n" +
                             "<pfn-to-lfn protocol=\"srm\"\n" +
                                "path-match=\".*\\?SFN=/11/store/(.*)\" result=\"/store/$1\"/>\n" +
                             "<lfn-to-pfn protocol=\"direct\"\n" +
                                "path-match=\"/+(.*)\" result=\"/castor/cern.ch/cms/$1\"/>\n" +
                             "<lfn-to-pfn protocol=\"srm\" chain=\"direct\"\n" +
                                "path-match=\"(.*)\" result=\"srm://srm.cern.ch/srm/managerv1?SFN=$1\"/>\n" +
                             "<pfn-to-lfn protocol=\"direct\"\n" +
                                "path-match=\"/+castor/cern\\.ch/cms/(.*)\" result=\"/$1\"/>\n" +
                             "<pfn-to-lfn protocol=\"srm\" chain=\"direct\"\n" +
                                "path-match=\".*\\?SFN=(.*)\" result=\"$1\"/>" +
                        "</storage-mapping>");

        Assert.assertEquals(6, map.size());
    }

    @Test
    public void shouldCombineChainedRules() {
        scala.collection.immutable.Map<String, String> map = CmsSettingsParser.parse(
                "<storage-mapping>\n" +
                        "<pfn-to-lfn protocol=\"direct\"\n" +
                        "path-match=\"/+castor/cern\\.ch/cms/(.*)\" result=\"/$1\"/>\n" +
                        "<pfn-to-lfn protocol=\"srm\" chain=\"direct\"\n" +
                        "path-match=\".*\\?SFN=(.*)\" result=\"$1\"/>" +
                "</storage-mapping>");

        String actual = map.get("srm://.*\\?SFN=/+castor/cern\\.ch/cms/(.*)").get();
        Assert.assertEquals("/$1", actual);
    }

    @Test
    public void shouldUnderstandDesyRules() {
        scala.collection.immutable.Map<String, String> map = CmsSettingsParser.parse(
                "<storage-mapping>\n" +
                        "\n" +
                        "<!-- Specific for Load Test 07 input files-->\n" +
                        "  <lfn-to-pfn protocol=\"srm\"\n" +
                        "    path-match=\".*/LoadTest07_DESY_(.*)_.*_.*\"\n" +
                        "    result=\"srm://dcache-se-cms.desy.de:8443/srm/managerv1?SFN=/pnfs/desy.de/cms/tier2/store/phedex_monarctest/monarctest_DESY/MonarcTest_DESY_$1\"/>\n" +
                        "<!-- For SRM2 -->\n" +
                        "  <lfn-to-pfn protocol=\"srmv2\"\n" +
                        "    path-match=\".*/LoadTest07_DESY_(.*)_.*_.*\"\n" +
                        "    result=\"srm://dcache-se-cms.desy.de:8443/srm/managerv2?SFN=/pnfs/desy.de/cms/tier2/store/phedex_monarctest/monarctest_DESY/MonarcTest_DESY_$1\"/>\n" +
                        "\n" +
                        "<!-- DCMS MC production -->\n" +
                        "  <lfn-to-pfn protocol=\"direct\"\n" +
                        "    path-match=\"/+store/DCMS/(.*)\"\n" +
                        "    result=\"/pnfs/desy.de/cms/analysis/dcms/$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"direct\"\n" +
                        "    path-match=\"/+store/unmerged/DCMS/(.*)\"\n" +
                        "    result=\"/pnfs/desy.de/cms/analysis/dcms/unmerged/$1\"/>\n" +
                        "  <pfn-to-lfn protocol=\"direct\"\n" +
                        "    path-match=\"/pnfs/desy.de/cms/analysis/dcms/unmerged/(.*)\"\n" +
                        "    result=\"/store/unmerged/DCMS/$1\"/>\n" +
                        "  <pfn-to-lfn protocol=\"direct\"\n" +
                        "    path-match=\"/pnfs/desy.de/cms/analysis/dcms/(.*)\"\n" +
                        "    result=\"/store/DCMS/$1\"/>\n" +
                        "\n" +
                        "<!-- Special directories -->\n" +
                        "  <lfn-to-pfn protocol=\"srm\"\n" +
                        "    path-match=\"/+store/PhEDEx_LoadTest07/(.*)\"\n" +
                        "    result=\"srm://dcache-se-cms.desy.de:8443/srm/managerv1?SFN=/pnfs/desy.de/cms/tier2/loadtest/$1\"/>\n" +
                        "  <pfn-to-lfn protocol=\"direct\"\n" +
                        "    path-match=\"/pnfs/desy.de/cms/tier2/loadtest/(.*)\"\n" +
                        "    result=\"/store/PhEDEx_LoadTest07/$1\"/>\n" +
                        "<!-- For SRM2 -->\n" +
                        "  <lfn-to-pfn protocol=\"srmv2\"\n" +
                        "    path-match=\"/+store/PhEDEx_LoadTest07/(.*)\"\n" +
                        "    result=\"srm://dcache-se-cms.desy.de:8443/srm/managerv2?SFN=/pnfs/desy.de/cms/tier2/loadtest/$1\"/>\n" +
                        "\n" +
                        "<!-- unmerged/temp directories -->\n" +
                        "  <lfn-to-pfn protocol=\"direct\"\n" +
                        "    path-match=\"/+store/unmerged/(.*)\"\n" +
                        "    result=\"/pnfs/desy.de/cms/tier2/unmerged/$1\"/>\n" +
                        "  <pfn-to-lfn protocol=\"direct\"\n" +
                        "    path-match=\"/pnfs/desy.de/cms/tier2/unmerged/(.*)\"\n" +
                        "    result=\"/store/unmerged/$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"direct\"\n" +
                        "    path-match=\"/+store/temp/(.*)\"\n" +
                        "    result=\"/pnfs/desy.de/cms/tier2/temp/$1\"/>\n" +
                        "  <pfn-to-lfn protocol=\"direct\"\n" +
                        "    path-match=\"/pnfs/desy.de/cms/tier2/temp/(.*)\"\n" +
                        "    result=\"/store/temp/$1\"/>\n" +
                        "\n" +
                        "<!-- Open JobRobot sample via xrootd - dirty hack! -->\n" +
                        "  <lfn-to-pfn protocol=\"dcap\"\n" +
                        "    path-match=\"/+store/mc/JobRobot/(.*)\"\n" +
                        "    result=\"root://dcache-cms-xrootd.desy.de/pnfs/desy.de/cms/tier2/store/mc/JobRobot/$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"dcap\"\n" +
                        "    path-match=\"/+store/mc/CMSSW_4_2_3/RelValProdTTbar/GEN-SIM-RECO/MC_42_V12_JobRobot-v1/(.*)\"\n" +
                        "    result=\"root://dcache-cms-xrootd.desy.de/pnfs/desy.de/cms/tier2/store/mc/CMSSW_4_2_3/RelValProdTTbar/GEN-SIM-RECO/MC_42_V12_JobRobot-v1/$1\"/>\n" +
                        "\n" +
                        "<!-- Normal stuff here -->\n" +
                        " <!-- remote xrootd fallback via Bari redirector -->\n" +
                        "  <lfn-to-pfn protocol=\"remote-xrootd\"\n" +
                        "    path-match=\"/+(.*)\"\n" +
                        "    result=\"root://xrootd.ba.infn.it//$1\"/>\n" +
                        "  <pfn-to-lfn protocol=\"remote-xrootd\"\n" +
                        "    path-match=\"/(.*)\"\n" +
                        "    result=\"/$1\"/>\n" +
                        " <!-- End of redirector stuff --> \n" +
                        "  <lfn-to-pfn protocol=\"direct\"\n" +
                        "    path-match=\"/+(.*)\"\n" +
                        "    result=\"/pnfs/desy.de/cms/tier2/$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"dcap\" chain=\"direct\"\n" +
                        "    path-match=\"(.*)\"\n" +
                        "    result=\"dcap://dcache-cms-dcap.desy.de/$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"gsidcap\" chain=\"direct\"\n" +
                        "    path-match=\"(.*)\"\n" +
                        "    result=\"gsidcap://dcache-cms-gsidcap.desy.de:22128/$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"srm\" chain=\"direct\"\n" +
                        "    path-match=\"(.*)\"\n" +
                        "    result=\"srm://dcache-se-cms.desy.de:8443/srm/managerv1?SFN=$1\"/>\n" +
                        "  <pfn-to-lfn protocol=\"direct\"\n" +
                        "    path-match=\"/pnfs/desy.de/cms/tier2/(.*)\"\n" +
                        "    result=\"/$1\"/>\n" +
                        "  <pfn-to-lfn protocol=\"srm\" chain=\"direct\"\n" +
                        "    path-match=\".*\\?SFN=(.*)\"\n" +
                        "    result=\"$1\"/>\n" +
                        "<!-- For SRM2 -->\n" +
                        "  <lfn-to-pfn protocol=\"srmv2\" chain=\"direct\"\n" +
                        "    path-match=\"(.*)\"\n" +
                        "    result=\"srm://dcache-se-cms.desy.de:8443/srm/managerv2?SFN=$1\"/>\n" +
                        "  <pfn-to-lfn protocol=\"srmv2\" chain=\"direct\"\n" +
                        "    path-match=\".*\\?SFN=(.*)\"\n" +
                        "    result=\"$1\"/>\n" +
                        "</storage-mapping>\n"
        );

        Assert.assertEquals(19, map.size());

        String actual = map.get("srmv2://.*\\?SFN=/pnfs/desy.de/cms/tier2/(.*)").get();
        Assert.assertEquals("/$1", actual);
    }

    @Test
    public void shouldUnderstandCernRules() {
        scala.collection.immutable.Map<String, String> map = CmsSettingsParser.parse(
                "<storage-mapping>\n" +
                        "  <!-- Specific for LoadTest07  -->\n" +
                        "  <lfn-to-pfn protocol=\"direct\"\n" +
                        "    path-match=\".*/LoadTest07_10GB_Source/CH_CERN_(.*).LTgenerated/T0_CH_CERN_Export/PhEDEx_Debug/LoadTest07_T0_CH_CERN_Export_10GB/.*\"\n" +
                        "    result=\"/castor/cern.ch/cms/store/PhEDEx_LoadTest07_10GB/LoadTest07_CERN10GB_$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"direct\"\n" +
                        "    path-match=\".*/LoadTest07_10GB_Source/CH_CERN_(.*)\"\n" +
                        "    result=\"/castor/cern.ch/cms/store/PhEDEx_LoadTest07_10GB/LoadTest07_CERN10GB_$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"direct\"\n" +
                        "    path-match=\".*/(LoadTest07_.*/T1_CH_CERN_Buffer/.*)\"\n" +
                        "    result=\"/castor/cern.ch/cms/store/PhEDEx_LoadTest07/$1\" />\n" +
                        "  <lfn-to-pfn protocol=\"direct\"\n" +
                        "    path-match=\".*/LoadTest07_.*_CERN_(.*)_.*_.*\"\n" +
                        "    result=\"/castor/cern.ch/cms/store/PhEDEx_LoadTest07_4/LoadTest07_CERN_$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"direct\"\n" +
                        "    path-match=\".*/LoadTest07Source/T._CERN_(.*)\"\n" +
                        "    result=\"/castor/cern.ch/cms/store/PhEDEx_LoadTest07_4/LoadTest07_CERN_$1\"/>\n" +
                        "\n" +
                        "  <!-- Map express data to t0express pool -->\n" +
                        "  <lfn-to-pfn protocol=\"rfio\"\n" +
                        "    path-match=\"/+store/express/(.*)\"\n" +
                        "    result=\"root://eoscms//eos/cms/store/express/$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"stageout\"\n" +
                        "    path-match=\"/+store/express/(.*)\"\n" +
                        "    result=\"root://eoscms//eos/cms/store/express/$1\"/>\n" +
                        "\n" +
                        "  <!-- Map any LFN with /store/caf/user to a PFN in the cmscafuser pool or EOS -->\n" +
                        "  <lfn-to-pfn protocol=\"rfio\"\n" +
                        "    path-match=\"/+store/caf/user/(.*)\"\n" +
                        "    result=\"root://eoscms//eos/cms/store/caf/user/$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"stageout\"\n" +
                        "    path-match=\"/+store/caf/user/(.*)\"\n" +
                        "    result=\"root://eoscms//eos/cms/store/caf/user/$1\"/>\n" +
                        "\n" +
                        "  <!-- Map any LFN with /store/group to a PFN on EOS -->\n" +
                        "  <lfn-to-pfn protocol=\"rfio\"\n" +
                        "    path-match=\"/+store/group/(.*)\"\n" +
                        "    result=\"root://eoscms//eos/cms/store/group/$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"stageout\"\n" +
                        "    path-match=\"/+store/group/(.*)\"\n" +
                        "    result=\"root://eoscms//eos/cms/store/group/$1\"/>\n" +
                        "\n" +
                        "  <!-- Map any LFN with /store/cmst3/ to EOS -->\n" +
                        "  <lfn-to-pfn protocol=\"rfio\"\n" +
                        "    path-match=\"/+store/cmst3/(.*)\"\n" +
                        "    result=\"root://eoscms//eos/cms/store/cmst3/$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"stageout\"\n" +
                        "    path-match=\"/+store/cmst3/(.*)\"\n" +
                        "    result=\"root://eoscms//eos/cms/store/cmst3/$1\"/>\n" +
                        "\n" +
                        "  <!-- Use /store/user/gowdy/rfio for testing RFIO -->\n" +
                        "  <lfn-to-pfn protocol=\"rfio\"\n" +
                        "    path-match=\"/+store/user/gowdy/rfio/(.*)\"\n" +
                        "    result=\"rfio://castorcms//castor/cern.ch/cms/store/user/gowdy/rfio/$1?svcClass=default\"/>\n" +
                        "  <lfn-to-pfn protocol=\"stageout\"\n" +
                        "    path-match=\"/+store/user/gowdy/rfio/(.*)\"\n" +
                        "    result=\"rfio://castorcms//castor/cern.ch/cms/store/user/gowdy/rfio/$1?svcClass=default\"/>\n" +
                        "\n" +
                        "  <!-- Map any LFN with /store/user to a PFN on EOS -->\n" +
                        "  <lfn-to-pfn protocol=\"stageout\"\n" +
                        "    path-match=\"/+store/user/(.*)\"\n" +
                        "    result=\"root://eoscms//eos/cms/store/user/$1\"/>\n" +
                        "\n" +
                        "  <!-- Map any LFN with /store/temp/user to a PFN on EOS -->\n" +
                        "  <lfn-to-pfn protocol=\"stageout\"\n" +
                        "    path-match=\"/+store/temp/user/(.*)\"\n" +
                        "    result=\"root://eoscms//eos/cms/store/temp/user/$1\"/>\n" +
                        "\n" +
                        "  <!-- Map any LFN with /store/unmerged/ to a PFN on EOS -->\n" +
                        "  <lfn-to-pfn protocol=\"stageout\"\n" +
                        "    path-match=\"/+store/unmerged/(.*)\"\n" +
                        "    result=\"root://eoscms//eos/cms/store/unmerged/$1\"/>\n" +
                        "\n" +
                        "  <!-- Map any LFN with /store/relval to a PFN on EOS -->\n" +
                        "  <lfn-to-pfn protocol=\"stageout\"\n" +
                        "    path-match=\"/+store/relval/(.*)\"\n" +
                        "    result=\"root://eoscms//eos/cms/store/relval/$1\"/>\n" +
                        "\n" +
                        "  <!-- Map any LFN with /T0/hufnagel/recotest/store/t0temp to a PFN on EOS -->\n" +
                        "  <lfn-to-pfn protocol=\"rfio\"\n" +
                        "    path-match=\"/+T0/hufnagel/recotest/store/t0temp/(.*)\"\n" +
                        "    result=\"root://eoscms//eos/cms/store/eos/user/hufnagel/t0temp/$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"direct\"\n" +
                        "    path-match=\"/+T0/hufnagel/recotest/store/t0temp/(.*)\"\n" +
                        "    result=\"/eos/cms/store/eos/user/hufnagel/t0temp/$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"stageout\"\n" +
                        "    path-match=\"/+T0/hufnagel/recotest/store/t0temp/(.*)\"\n" +
                        "    result=\"root://eoscms//eos/cms/store/eos/user/hufnagel/t0temp/$1\"/>\n" +
                        "\n" +
                        "  <!-- Map any LFN with /store/t0streamer to xrootd on t0streamer -->\n" +
                        "  <lfn-to-pfn  protocol=\"rfio\"\n" +
                        "    path-match=\"/+store/t0streamer/(.*)\"\n" +
                        "    result=\"root://castorcms//castor/cern.ch/cms/store/t0streamer/$1?svcClass=t0streamer&amp;stageHost=castorcms\"/>\n" +
                        "  <lfn-to-pfn  protocol=\"stageout\"\n" +
                        "    path-match=\"/+store/t0streamer/(.*)\"\n" +
                        "    result=\"rfio://castorcms//castor/cern.ch/cms/store/t0streamer/$1?svcClass=t0streamer\"/>\n" +
                        "\n" +
                        "  <!-- Map any LFN with /store/t0temp to xrootd on EOS -->\n" +
                        "  <lfn-to-pfn protocol=\"rfio\"\n" +
                        "    path-match=\"/+store/t0temp/(.*)\"\n" +
                        "    result=\"root://eoscms//eos/cms/store/t0temp/$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"direct\"\n" +
                        "    path-match=\"/+store/t0temp/(.*)\"\n" +
                        "    result=\"/eos/cms/store/t0temp/$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"stageout\"\n" +
                        "    path-match=\"/+store/t0temp/(.*)\"\n" +
                        "    result=\"root://eoscms//eos/cms/store/t0temp/$1\"/>\n" +
                        "\n" +
                        "  <!-- Specific for MC at CERN, only T2 -->\n" +
                        "  <lfn-to-pfn protocol=\"stageout\"\n" +
                        "    path-match=\"/+store/mc/(.*)\"\n" +
                        "    result=\"root://eoscms//eos/cms/store/mc/$1\"/>\n" +
                        "\n" +
                        "  <!-- user/group data on EOS -->\n" +
                        "  <lfn-to-pfn protocol=\"stageout\"\n" +
                        "    path-match=\"/+store/eos/(.*)\"\n" +
                        "    result=\"root://eoscms//eos/cms/store/eos/$1\"/>\n" +
                        "\n" +
                        "  <!-- LHE data on EOS -->\n" +
                        "  <lfn-to-pfn protocol=\"stageout\"\n" +
                        "    path-match=\"/+store/lhe/(.*)\"\n" +
                        "    result=\"root://eoscms//eos/cms/store/lhe/$1\"/>\n" +
                        "\n" +
                        "  <!-- Tier0 test data on EOS -->\n" +
                        "\n" +
                        "  <lfn-to-pfn path-match=\"/+store/backfill/3/(.*)\" protocol=\"rfio\"\n" +
                        "     result=\"root://eoscms//eos/cms/store/backfill/3/$1\"/>\n" +
                        "  <lfn-to-pfn path-match=\"/+store/backfill/3/(.*)\" protocol=\"stageout\"\n" +
                        "     result=\"root://eoscms//eos/cms/store/backfill/3/$1\"/>\n" +
                        "\n" +
                        "  <lfn-to-pfn path-match=\"/+store/backfill/2/t0temp/(.*)\" protocol=\"rfio\"\n" +
                        "     result=\"root://eoscms//eos/cms/store/backfill/2/t0temp/$1\"/>\n" +
                        "  <lfn-to-pfn path-match=\"/+store/backfill/2/t0temp/(.*)\" protocol=\"stageout\"\n" +
                        "     result=\"root://eoscms//eos/cms/store/backfill/2/t0temp/$1\"/>\n" +
                        "\n" +
                        "  <lfn-to-pfn path-match=\"/+store/backfill/2/express/(.*)\" protocol=\"rfio\"\n" +
                        "     result=\"root://eoscms//eos/cms/store/backfill/2/express/$1\"/>\n" +
                        "  <lfn-to-pfn path-match=\"/+store/backfill/2/express/(.*)\" protocol=\"stageout\"\n" +
                        "     result=\"root://eoscms//eos/cms/store/backfill/2/express/$1\"/>\n" +
                        "\n" +
                        "  <lfn-to-pfn path-match=\"/+store/backfill/(.*)\" protocol=\"rfio\"\n" +
                        "     result=\"root://castorcms//castor/cern.ch/cms/store/backfill/$1?svcClass=t0export&amp;stageHost=castorcms\"/>\n" +
                        "  <lfn-to-pfn path-match=\"/+store/backfill/(.*)\" protocol=\"stageout\"\n" +
                        "     result=\"rfio://castorcms//castor/cern.ch/cms/store/backfill/$1\"/>\n" +
                        "\n" +
                        "  <!-- Map pretend LFN /user to CASTOR User Space -->\n" +
                        "  <lfn-to-pfn protocol=\"rfio\"\n" +
                        "    path-match=\"/+user/(.*)\"\n" +
                        "    result=\"root://castorcms//castor/cern.ch/user/$1?svcClass=default\"/>\n" +
                        "  <lfn-to-pfn protocol=\"stageout\"\n" +
                        "    path-match=\"/+user/(.*)\"\n" +
                        "    result=\"root://castorcms//castor/cern.ch/user/$1?svcClass=default\"/>\n" +
                        "\n" +
                        "\n" +
                        "  <!-- Map any LFN with /store/ to xrootd on EOS, fall back to default -->\n" +
                        "  <lfn-to-pfn protocol=\"rfio\"\n" +
                        "    path-match=\"/+store/(.*)\"\n" +
                        "    result=\"root://eoscms//eos/cms/store/$1?svcClass=default\"/>\n" +
                        "\n" +
                        "  <!-- Below is the default rule -->\n" +
                        "  <lfn-to-pfn protocol=\"direct\"\n" +
                        "    path-match=\"/+(.*)\"\n" +
                        "    result=\"/castor/cern.ch/cms/$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"stageout\" chain=\"direct\"\n" +
                        "    path-match=\"(.*)\"\n" +
                        "    result=\"$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"rfio\" chain=\"direct\"\n" +
                        "    path-match=\"(.*)\"\n" +
                        "    result=\"rfio:$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"srmv2\"\n" +
                        "    path-match=\"/+store/user/(.*)\"\n" +
                        "    result=\"srm://srm-eoscms.cern.ch:8443/srm/v2/server?SFN=/eos/cms/store/user/$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"srmv2\" chain=\"direct\" destination-match=\"T1_CH_CERN_.*\" is-custodial=\"y\"\n" +
                        "    path-match=\"(.*)\"\n" +
                        "    result=\"srm://srm-cms.cern.ch:8443/srm/managerv2?SFN=$1\"\n" +
                        "    space-token=\"CMS_DEFAULT\"/>\n" +
                        "  <lfn-to-pfn protocol=\"srmv2\" chain=\"direct\" destination-match=\"T1_CH_CERN_.*\" is-custodial=\"n\"\n" +
                        "    path-match=\"(.*)\"\n" +
                        "    result=\"srm://srm-cms.cern.ch:8443/srm/managerv2?SFN=$1\"\n" +
                        "    space-token=\"CMS_DEFAULT\"/>\n" +
                        "  <lfn-to-pfn protocol=\"srmv2\" chain=\"direct\" destination-match=\"T2_CH_CAF\"\n" +
                        "    path-match=\"(.*)\"\n" +
                        "    result=\"srm://srm-cms.cern.ch:8443/srm/managerv2?SFN=$1\"\n" +
                        "    space-token=\"CMS_CAF\"/>\n" +
                        "  <lfn-to-pfn protocol=\"srmv2\" chain=\"direct\" destination-match=\"T2_CH_CERN\"\n" +
                        "    path-match=\"(.*)\"\n" +
                        "    result=\"srm://srm-eoscms.cern.ch:8443/srm/v2/server?SFN=$1\"/>\n" +
                        "  <lfn-to-pfn protocol=\"srmv2\" chain=\"direct\"\n" +
                        "    path-match=\"(.*)\"\n" +
                        "    result=\"srm://srm-cms.cern.ch:8443/srm/managerv2?SFN=$1\"/>\n" +
                        "\n" +
                        "  <pfn-to-lfn protocol=\"direct\"\n" +
                        "    path-match=\"/+castor/cern\\.ch/cms/(.*)\"\n" +
                        "    result=\"/$1\"/>\n" +
                        "  <pfn-to-lfn protocol=\"rfio\" chain=\"direct\"\n" +
                        "    path-match=\"^rfio:/+(.*)\"\n" +
                        "    result=\"/$1\"/>\n" +
                        "  <pfn-to-lfn protocol=\"srmv2\" chain=\"direct\"\n" +
                        "    path-match=\".*\\?SFN=(.*)\"\n" +
                        "    result=\"$1\"/>\n" +
                        "\n" +
                        "</storage-mapping>");

        Assert.assertEquals(3, map.size());
    }



}
