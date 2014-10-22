package com.timvergenz.chemturing.util

import org.scalatest.Matchers
import org.scalatest.FlatSpec

class UrlEncodedTest extends FlatSpec with Matchers {

  val base = StateGenerators.blankState(20)
  
  "UrlEncoded" should "convert State to String" in {
    UrlEncoded(base) should be ("00000000000000000000/0/0/0/EXEC/0/5")
  }
  it should "convert String to State" in {
    UrlEncoded.unapply("00000000000000000000/0/0/0/EXEC/0/5").get should be (base)
  }

}