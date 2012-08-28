package org.dcache.xrootd4j;

import org.junit.*;

import java.util.Properties;

public class CmsMappingProviderTest {

    CmsMappingProvider provider;

    @Before
    public void setup() {
       provider = new CmsMappingProvider();
    }

    @After
    public void tearDown() {
        provider = null;
    }

    @Test
    public void shouldReturnFactoryForMatchingName() {
        Properties properties = new Properties();
        properties.put(CmsMappingFactory.FILENAME_PROPERTY(), "settings.xml");
        CmsMappingFactory factory = provider.createFactory(CmsMappingFactory.NAME(), properties);
        Assert.assertNotNull(factory);
    }

    @Test
    public void shouldReturnNullForNotMatchingName() {
        CmsMappingFactory factory = provider.createFactory("other", new Properties());
        Assert.assertNull(factory);
    }

}
