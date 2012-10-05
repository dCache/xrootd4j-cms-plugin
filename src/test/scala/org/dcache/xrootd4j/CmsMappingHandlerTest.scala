/**
 *  This file is part of xrootd4j-cms-plugin.
 *
 *  xrootd4j-cms-plugin is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  xrootd4j-cms-plugin is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 *  of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with xrootd4j-cms-plugin.  If not, see
 *  <http://www.gnu.org/licenses/.>
 *
 */
package org.dcache.xrootd4j

import org.scalatest._
import junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class CmsMappingHandlerTest extends FlatSpec {

  val map = CmsSettingsParser.parse(
    <storage-mapping>
      <lfn-to-pfn protocol="direct" path-match="sub1/(.*)" result="subchain1/$1"/>
      <lfn-to-pfn protocol="direct" path-match="sub2/(.*)" result="subchain2/$1"/>
      <lfn-to-pfn protocol="direct" path-match="role/path/(.*)" result="correctpath/$1"/>
      <lfn-to-pfn protocol="root" path-match="/simple/(.*)" result="$1"/>
      <lfn-to-pfn protocol="root" chain="direct" path-match="/chained/(.*)" result="/chainbase/$1"/>
      <lfn-to-pfn protocol="root" path-match="/chained/role/(.*)" result="/wrongpath/$1"/>
      <lfn-to-pfn protocol="root" path-match="/one/(.*)/two/(.*)" result="$1$2"/>
    </storage-mapping>)

  val handler = new CmsMappingHandler(map, "root")
  val mappedFilenameFor = handler.authorize(null, null, null, _ : String, null, 0, null)

  it should "leave an unmatched path unchanged" in {
    expect("root://unmatched/blubb") {
      mappedFilenameFor("root://unmatched/blubb")
    }
  }

  it should "map a matching path" in {
    expect("wurstbrot") {
      mappedFilenameFor("/simple/wurstbrot")
    }
  }

  it should "map a matching chained rule" in {
    expect("/chainbase/subchain1/wurstbrot") {
      mappedFilenameFor("/chained/sub1/wurstbrot")
    }
  }

  it should "use the first matching rule" in {
    expect("/chainbase/correctpath/wurstbrot") {
      mappedFilenameFor("/chained/role/path/wurstbrot")
    }
  }

  it should "be able to understand patterns with two groups" in {
    expect("firstsecond") {
      mappedFilenameFor("/one/first/two/second")
    }
  }
}
