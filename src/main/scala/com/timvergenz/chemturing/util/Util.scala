package com.timvergenz.chemturing.util

object Util {
  implicit def string2BitSeq(str: String): Seq[Boolean] = {
    str.toCharArray().map({case '0' => true; case '1' => false})
  }
}
