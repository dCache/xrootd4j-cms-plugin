package org.dcache.xrootd4j;

import org.junit.*;

public class CmsMappingHandlerTest {

    CmsMappingHandler handler;

    @Before
    public void setup() {

        handler = new CmsMappingHandler(CmsSettingsParser.parse(
                "<storage-mapping>\n" +
                        "\n" +
                        "  <pfn-to-lfn protocol=\"direct\"\n" +
                        "    path-match=\"/pnfs/desy.de/cms/analysis/dcms/unmerged/(.*)\"\n" +
                        "    result=\"/store/unmerged/DCMS/$1\"/>\n" +
                        "  <pfn-to-lfn protocol=\"direct\"\n" +
                        "    path-match=\"/pnfs/desy.de/cms/analysis/dcms/(.*)\"\n" +
                        "    result=\"/store/DCMS/$1\"/>\n" +
                        "  <pfn-to-lfn protocol=\"direct\"\n" +
                        "    path-match=\"/pnfs/desy.de/cms/tier2/loadtest/(.*)\"\n" +
                        "    result=\"/store/PhEDEx_LoadTest07/$1\"/>\n" +
                        "  <pfn-to-lfn protocol=\"direct\"\n" +
                        "    path-match=\"/pnfs/desy.de/cms/tier2/unmerged/(.*)\"\n" +
                        "    result=\"/store/unmerged/$1\"/>\n" +
                        "  <pfn-to-lfn protocol=\"direct\"\n" +
                        "    path-match=\"/pnfs/desy.de/cms/tier2/temp/(.*)\"\n" +
                        "    result=\"/store/temp/$1\"/>\n" +
                        "  <pfn-to-lfn protocol=\"remote-xrootd\"\n" +
                        "    path-match=\"/(.*)\"\n" +
                        "    result=\"/$1\"/>\n" +
                        "  <pfn-to-lfn protocol=\"direct\"\n" +
                        "    path-match=\"/pnfs/desy.de/cms/tier2/(.*)\"\n" +
                        "    result=\"/$1\"/>\n" +
                        "  <pfn-to-lfn protocol=\"srm\" chain=\"direct\"\n" +
                        "    path-match=\".*\\?SFN=(.*)\"\n" +
                        "    result=\"$1\"/>\n" +
                        "  <pfn-to-lfn protocol=\"srmv2\" chain=\"direct\"\n" +
                        "    path-match=\".*\\?SFN=(.*)\"\n" +
                        "    result=\"$1\"/>\n" +
                "</storage-mapping>"));
    }

    @Test
    public void shouldLeaveUnmatchedProtocolUnchanged() {
        String result =handler.authorize(null, null, null,
                "ftp://store/blubb",
                null, 0, null);

        Assert.assertEquals("ftp://store/blubb", result);
    }

    @Test
    public void shouldLeaveUnmatchedPathUnchanged() {
        String result =handler.authorize(null, null, null,
                "srm://unmatched/blubb",
                null, 0, null);

        Assert.assertEquals("srm://unmatched/blubb", result);
    }

    @Test
    public void shouldMapMatchingPath() {
        String result = handler.authorize(null, null, null,
                "remote-xrootd:///pnfs/fs/usr/cms/WAX/11/store/wurstbrot",
                null, 0, null);

        Assert.assertEquals("/pnfs/fs/usr/cms/WAX/11/store/wurstbrot", result);
    }

    @Test
    public void shouldMapWithChainedRule() {
        String result = handler.authorize(null, null, null,
                "srm://schwupp/ti/du?SFN=/pnfs/desy.de/cms/tier2/temp/wurstbrot",
                null, 0, null);

        Assert.assertEquals("/store/temp/wurstbrot", result);
    }
}
