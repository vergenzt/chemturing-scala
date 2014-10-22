package com.timvergenz.chemturing.util

import com.timvergenz.chemturing.core._
import com.timvergenz.chemturing.util.StringInterpolaters._

object StateGenerators {

  private val DEFAULT_SIZE = 20

  /**
   * Generate a blank, empty ChemTuring state.
   */
  def blankState(n: Int = DEFAULT_SIZE): State = {
    State(
      data = (1 to n) map (i => bit"0"),
      mem = bit"0",
      m = 5,
      progPtr = 0,
      dataPtr = 0,
      mode = EXEC,
      prep = bit"0"
    )
  }

  /**
   * Generate a random ChemTuring state.
   */
  def randomStartState(n: Int = DEFAULT_SIZE): State = {
    ???
  }

}
