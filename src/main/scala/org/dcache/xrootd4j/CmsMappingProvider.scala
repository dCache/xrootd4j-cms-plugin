package org.dcache.xrootd4j

import java.util.Properties

import org.dcache.xrootd.plugins.AuthorizationProvider

class CmsMappingProvider extends AuthorizationProvider
{
    @Override
    def createFactory(plugin : String, properties : Properties) : CmsMappingFactory =
    {
        if (CmsMappingFactory hasName plugin) new CmsMappingFactory(properties) else null
    }
}