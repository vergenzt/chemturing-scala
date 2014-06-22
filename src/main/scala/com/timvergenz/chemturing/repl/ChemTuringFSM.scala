package com.timvergenz.chemturing.repl

import com.timvergenz.chemturing.core.State

import akka.actor._

sealed trait ChemTuringFSMCommand
case class SetState(val state: State) extends ChemTuringFSMCommand
case class Step(val n: Int) extends ChemTuringFSMCommand
case object GetStateString extends ChemTuringFSMCommand

sealed trait ChemTuringFSMState
case object Uninitialized extends ChemTuringFSMState
case object Running extends ChemTuringFSMState

class ChemTuringFSM extends Actor
  with ActorLogging
  with FSM[ChemTuringFSMState, State] {

  startWith(Running, null)

  when(Uninitialized) {
    case Event(SetState(state), _) =>
      goto(Running) using state
  }

  when(Running) {
    case Event(SetState(state), _) =>
      log.info("Overwriting previous state")
      stay using state

    case Event(Step(1), state) =>
      stay using state.next

    case Event(GetStateString, state) =>
      stay replying (state.toString)

  }
}
