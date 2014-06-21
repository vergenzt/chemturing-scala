package com.timvergenz.chemturing.core

import org.scalatest.FlatSpec
import org.scalatest.Matchers

import com.timvergenz.chemturing.util.Util.string2BitSeq
import com.timvergenz.chemturing.util.Util.char2Bit

class ChemTuringTest extends FlatSpec with Matchers {

  val ctBase = ChemTuringState()

  "A ChemTuringState" should "initialize correctly with default parameters" in {
    val ct = ctBase.copy(
      data = "00000000000000000000",
      mem = '0',
      m = 5,
      progPtr = 0,
      dataPtr = 0,
      mode = EXEC,
      prep = false)
    import ct._
    (data, mem, m, progPtr, dataPtr, mode, prep) should be
      (string2BitSeq("00000000000000000000"), char2Bit('0'), 5, 0, 0, EXEC, false)
  }

}
