package com.timvergenz.chemturing.util

import org.scalatest.Matchers
import org.scalatest.FlatSpec

class UtilTest extends FlatSpec with Matchers {

  "string2BitSeq" should "correctly convert a String to a Seq[Boolean]" in {
    Util.string2BitSeq("") should be (Seq())
    Util.string2BitSeq("0") should be (Seq(false))
    Util.string2BitSeq("1") should be (Seq(true))
    Util.string2BitSeq("0000") should be (Seq(false, false, false, false))
    Util.string2BitSeq("1010") should be (Seq(true, false, true, false))
    Util.string2BitSeq("1111") should be (Seq(true, true, true, true))
    Util.string2BitSeq("10100010111") should be
      (Seq(true, false, true, false, false, false, true, false, true, true, true))
    a [MatchError] should be thrownBy { Util.string2BitSeq("abc") }
  }

  "char2Bit" should "correctly convert a Char to a Boolean" in {
    Util.char2Bit('0') should be (false)
    Util.char2Bit('1') should be (true)

    a [MatchError] should be thrownBy { Util.char2Bit(' ') }
    a [MatchError] should be thrownBy { Util.char2Bit('a') }
    a [MatchError] should be thrownBy { Util.char2Bit('5') }
    a [MatchError] should be thrownBy { Util.char2Bit('\0') }
  }

}
