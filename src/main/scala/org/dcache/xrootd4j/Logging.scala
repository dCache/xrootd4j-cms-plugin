package org.dcache.xrootd4j

import org.slf4j.LoggerFactory

trait Logging {

  val logger = LoggerFactory.getLogger(this.getClass)

  def log[T](logOp : (String => Unit))(s : T) : T = {
    logOp(s.toString)
    s
  }

}
