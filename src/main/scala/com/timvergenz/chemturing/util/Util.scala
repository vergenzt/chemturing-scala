package com.timvergenz.chemturing.util

object Util {
  implicit def string2BitSeq(str: String): Seq[Boolean] = {
    str.toCharArray().map(char2Bit(_))
  }

  implicit def char2Bit(ch: Char): Boolean = {
    ch match {case '0' => false; case '1' => true}
  }
}
