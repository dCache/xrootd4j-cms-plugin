package org.dcache.xrootd4j

import com.google.common.base.Preconditions
import com.google.common.io.Files
import com.google.common.base.Charsets
import org.dcache.xrootd.plugins.AuthorizationFactory
import java.util.Properties
import java.io.File

object CmsMappingFactory
{
    val NAME = "xrootd4j.cms.settings"

    val ALTERNATIVE_NAMES = Set(NAME)

    val FILENAME_PROPERTY = "xrootd.cms.storage.path"

    def hasName(name : String) = (ALTERNATIVE_NAMES contains name)
}

class CmsMappingFactory(properties : Properties) extends AuthorizationFactory
{
    val filename = Preconditions.checkNotNull(properties.getProperty(CmsMappingFactory.FILENAME_PROPERTY))

    @Override
    def getName = CmsMappingFactory.NAME

    @Override
    def getDescription = "CMS Name Mapping Plugin"

    @Override
    def createHandler = {
        val file = new File(filename)
        val xmlString = ("" /: List(Files.readLines(file, Charsets.UTF_8)))(_ + _)
        val mappings = CmsSettingsParser.parse(xmlString)
        new CmsMappingHandler(mappings)
    }
}
