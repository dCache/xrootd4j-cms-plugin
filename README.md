Authorization and mapping plugin for xrootd4j and dCache
========================================================

This is an authorization and mapping plugin for xrootd4j and dCache.

To compile the plugin, run:

    mvn package


The plugin can be tested by loading it with the xrootd4j standalone
server available from http://github.com/dCache/xrootd4j:

    java -Dlog=debug 
         -jar /path/to/xrootd4j/xrootd4j-standalone-1.0.1-jar-with-dependencies.jar \
         --plugins target/xrootd4j-cms-plugin-1.0-SNAPSHOT/ \
         --authz org.dcache.xrootd4j
