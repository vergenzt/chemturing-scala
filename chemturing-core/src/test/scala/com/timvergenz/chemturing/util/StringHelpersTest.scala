package com.timvergenz.chemturing.util

import org.scalatest.Matchers
import org.scalatest.FlatSpec

class StringHelpersTest extends FlatSpec with Matchers {
  
  "Bit" should "convert String to Boolean" in {
    Bit("0") should be (false)
    Bit("1") should be (true)
  }
  it should "convert Char to Boolean" in {
    Bit('0') should be (false)
    Bit('1') should be (true)
  }
  it should "convert Boolean to String" in {
    Bit.unapply(false).get should be ("0")
    Bit.unapply(true).get should be ("1")
  }
  
  "BitSeq" should "convert String to Seq[Boolean]" in {
    BitSeq("001001") should be (Seq(false, false, true, false, false, true))
  }

}
