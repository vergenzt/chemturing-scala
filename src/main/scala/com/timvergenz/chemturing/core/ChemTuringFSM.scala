package com.timvergenz.chemturing.core

import akka.actor.Actor
import akka.actor.FSM
import akka.actor.ActorSystem
import akka.actor.Props

sealed trait ChemTuringFSMCommand
case class Step(val n: Int) extends ChemTuringFSMCommand
case object PrintState extends ChemTuringFSMCommand

sealed trait ChemTuringFSMState
case object Running extends ChemTuringFSMState

class ChemTuringFSM(val initialState: ChemTuring.State)
  extends Actor with FSM[ChemTuringFSMState, ChemTuring.State] {
  
  import ChemTuring._
  
  startWith(Running, initialState)

  when(Running) {

    case Event(Step(1), state) =>
      stay using state.next
      
    case Event(PrintState, state) =>
      println(state)
      stay

  }
}

object ChemTuringFSM extends App {

  val initialState = StateGenerators.blankStartState()

  val system = ActorSystem("ChemTuringSimulationSystem")
  val simulator = system.actorOf(Props(classOf[ChemTuringFSM], initialState), "simulator")
  
}
