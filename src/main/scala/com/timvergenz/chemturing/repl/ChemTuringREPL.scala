package com.timvergenz.chemturing.repl

import scala.tools.nsc.interpreter.ILoop
import scala.tools.nsc.Settings

class ChemTuringREPLImpl extends ILoop {
  addThunk {
    intp.beQuietDuring {
      intp.addImports("com.timvergenz.chemturing.core._")
      intp.addImports("com.timvergenz.chemturing.util._")
      intp.addImports("com.timvergenz.chemturing.util.StateGenerators._")
      intp.addImports("com.timvergenz.chemturing.util.Util._")
    }
  }

  override def prompt = "==> "

  override def printWelcome() {
    def msg = """
      |Welcome to ChemTuring v3.0!
      |
      """

    echo(msg.trim.stripMargin)
  }
}

object ChemTuringREPL {
  def main(args: Array[String]) {
    val settings = new Settings
    settings.usejavacp.value = true
    settings.deprecation.value = true

    new ChemTuringREPLImpl().process(settings)

    /*
    val initialState = StateGenerators.blankStartState()

    val system = ActorSystem("ChemTuringSimulationSystem")
    val simulator = system.actorOf(Props(classOf[ChemTuringFSM], initialState), "simulator")

    simulator ! Step(1)
    //simulator ! PrintState

    Thread.sleep(2000)
    system.shutdown()
    */
  }
}
