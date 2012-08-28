package org.dcache.xrootd4j

import java.net.InetSocketAddress
import javax.security.auth.Subject

import org.dcache.xrootd.protocol.XrootdProtocol.FilePerm
import org.dcache.xrootd.plugins.AuthorizationHandler
import util.matching.Regex

class CmsMappingHandler(mappings : Map[String, String]) extends AuthorizationHandler
{
    @Override
    def authorize(subject : Subject,
                  localAddress : InetSocketAddress,
                  remoteAddress : InetSocketAddress,
                  path : String,
                  opaque : java.util.Map[String, String],
                  request : Int,
                  mode : FilePerm) : String =
    {
        val applicableRule = mappings.find(rule => rule._1.r.findFirstIn(path).isDefined)

        if (applicableRule.isDefined) {
          applicableRule.get._1.r.replaceFirstIn(path, applicableRule.get._2)
        } else path
    }
}
