package org.dcache.xrootd4j;

import org.junit.*;

import java.util.Properties;

public class CmsMappingFactoryTest {

    @Test
    public void shouldHaveCorrectName() {
        Assert.assertTrue(CmsMappingFactory.hasName("xrootd4j.cms.settings"));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullProperties() {
        new CmsMappingFactory(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowIllegalArgumentExceptionForMissingFilenameProperty() {
        new CmsMappingFactory(new Properties());
    }

    @Test
    public void shouldCreateCmsMappingHandler() {
        Properties properties = new Properties();
        properties.put("xrootd.cms.storage.path", "settings.xml");
        Assert.assertNotNull(new CmsMappingFactory(properties));
    }

}
