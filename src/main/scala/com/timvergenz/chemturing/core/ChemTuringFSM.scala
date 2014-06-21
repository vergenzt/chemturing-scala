package com.timvergenz.chemturing.core

import akka.actor.FSM

sealed trait ChemTuringFSMCommand
case object Advance extends ChemTuringFSMCommand

sealed trait ChemTuringFSMState
case object Running extends ChemTuringFSMState


class ChemTuringFSM(val initialState: ChemTuringState)
  extends FSM[ChemTuringFSMState, ChemTuringState] {
  
  /*
   * Since each of the operations is defined as a PartialFunction, composing
   * them gives us a single function over the whole space of ChemTuringStates.
   */
  val totalOpsFunction =
    READ orElse
    WRITE orElse
    SKIP orElse
    MSKIP
  
  startWith(Running, initialState)

  when(Running) {
    ???
  }

}
