package com.timvergenz.chemturing.core

import com.timvergenz.chemturing.util.Util.char2Bit
import com.timvergenz.chemturing.util.Util.string2BitSeq

object StateGenerators {

  import ChemTuring._
  
  private val DEFAULT_SIZE = 20

  /**
   * Generate a blank, empty ChemTuring state.
   */
  def blankStartState(n: Int = DEFAULT_SIZE): State = {
    State(
      data = ("0" * n),
      mem = '0',
      m = 5,
      progPtr = 0,
      dataPtr = 0,
      mode = EXEC,
      prep = false)
  }

  /**
   * Generate a random ChemTuring state.
   */
  def randomStartState(n: Int = DEFAULT_SIZE): State = {
    ???
  }

}