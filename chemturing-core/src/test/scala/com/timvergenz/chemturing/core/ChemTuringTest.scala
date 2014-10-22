package com.timvergenz.chemturing.core

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import com.timvergenz.chemturing.util._
import com.timvergenz.chemturing.util.StringInterpolaters._

class ChemTuringTest extends FlatSpec with Matchers {

  val base = StateGenerators.blankState(20)

  "A ChemTuring.State" should "initialize correctly with default parameters" in {
    val state = State(
      data = seq"00000000000000000000",
      mem = bit"0",
      m = 5,
      progPtr = 0,
      dataPtr = 0,
      mode = EXEC,
      prep = false)
    import state._
    (data, mem, m, progPtr, dataPtr, mode, prep) should be
      (seq"00000000000000000000", bit"0", 5, 0, 0, EXEC, false)
  }

  it should "run a READ command correctly" in {
    val state = base.copy(
      data = READ.bitSeq ++ seq"10",
      dataPtr = 2,
      mem = bit"0",
      m = 2
    )
    val next = state.next
    next.data should be (state.data)
    next.mem should be (bit"1")
    next.dataPtr should be (3)
    next.progPtr should be (2)
  }

  it should "run a WRITE command correctly" in {
    val state = base.copy(
      data = WRITE.bitSeq ++ seq"10",
      dataPtr = 2,
      mem = bit"0",
      m = 2
    )
    val next = state.next
    next.data should be (WRITE.bitSeq ++ seq"00")
    next.mem should be (bit"0")
    next.dataPtr should be (3)
    next.progPtr should be (2)
  }

  it should "run a SKIP command correctly" in {
    val state = base.copy(
      data = SKIP.bitSeq ++ seq"10",
      dataPtr = 2,
      mem = bit"0",
      m = 2
    )
    val next = state.next
    next.data should be (SKIP.bitSeq ++ seq"10")
    next.mem should be (bit"0")
    next.dataPtr should be (3)
    next.progPtr should be (2)
  }

  it should "run an MSKIP command correctly" in {
    val state = base.copy(
      data = MSKIP.bitSeq ++ seq"00000000",
      dataPtr = 2,
      mem = bit"0",
      m = 5
    )
    val next = state.next
    next.data should be (state.data)
    next.mem should be (bit"0")
    next.dataPtr should be (7)
    next.progPtr should be (2)
  }

  it should "transition from EXEC to KILL correctly" in {
    val state = base.copy(
      data = READ.bitSeq ++ READ.bitSeq ++ seq"100000",
      mode = EXEC,
      progPtr = 0,
      dataPtr = 4,
      mem = bit"0",
      m = 5,
      prep = false
    )
    var states = List(state)

    states = states.head.next :: states
    states.head.data should be (state.data)
    states.head.mem should be (bit"1")
    states.head.dataPtr should be (5)
    states.head.progPtr should be (2)
    states.head.mode should be (EXEC)
    states.head.prep should be (true)

    states = states.head.next :: states
    states.head.data should be (state.data)
    states.head.mem should be (states(1).mem)
    states.head.dataPtr should be (6)
    states.head.progPtr should be (4)
    states.head.mode should be (KILL)
    states.head.prep should be (false)
  }

  it should "transition from KILL to EXEC correctly" in {
    val state = base.copy(
      data = WRITE.bitSeq ++ WRITE.bitSeq ++ READ.bitSeq ++ seq"0010",
      mode = KILL,
      progPtr = 0,
      dataPtr = 6,
      mem = bit"0",
      m = 5,
      prep = false
    )
    var states = List(state)

    states = states.head.next :: states
    states.head.data should be (state.data)
    states.head.mem should be (bit"0")
    states.head.dataPtr should be (7)
    states.head.progPtr should be (2)
    states.head.mode should be (KILL)
    states.head.prep should be (true)

    states = states.head.next :: states
    states.head.data should be (state.data)
    states.head.mem should be (states(1).mem)
    states.head.dataPtr should be (8)
    states.head.progPtr should be (4)
    states.head.mode should be (EXEC)
    states.head.prep should be (false)

    states = states.head.next :: states
    states.head.data should be (state.data)
    states.head.mem should be (bit"1")
    states.head.dataPtr should be (9)
    states.head.progPtr should be (6)
    states.head.mode should be (EXEC)
    states.head.prep should be (true)
  }

  it should "wrap the data pointer and program pointer around correctly" in {
    val state = base.copy(
      data = SKIP.bitSeq ++ SKIP.bitSeq ++ SKIP.bitSeq,
      mode = EXEC,
      progPtr = 0,
      dataPtr = 4,
      mem = bit"0",
      m = 2,
      prep = false
    )
    var states = List(state)

    states = states.head.next :: states
    states.head.dataPtr should be (5)
    states.head.progPtr should be (2)
    states = states.head.next :: states
    states.head.dataPtr should be (0)
    states.head.progPtr should be (4)
    states = states.head.next :: states
    states.head.dataPtr should be (1)
    states.head.progPtr should be (0)
    states = states.head.next :: states
    states.head.dataPtr should be (2)
    states.head.progPtr should be (2)
  }

}
