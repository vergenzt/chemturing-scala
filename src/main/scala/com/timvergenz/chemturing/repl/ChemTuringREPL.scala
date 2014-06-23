package com.timvergenz.chemturing.repl

import scala.tools.nsc.interpreter.ILoop
import scala.util.Properties.{ jdkHome, javaVersion, versionString, javaVmName }
import scala.tools.nsc.Settings

class ChemTuringREPLImpl extends ILoop {

  override def createInterpreter() = {
    super.createInterpreter()
    intp.initialize {
      intp.beQuietDuring {
        // initial commands to run for the session
        intp.interpret("import com.timvergenz.chemturing.core._")
        intp.interpret("import com.timvergenz.chemturing.util._")
        intp.interpret("import com.timvergenz.chemturing.util.StateGenerators._")
        intp.interpret("import com.timvergenz.chemturing.util.Util._")
      }
    }
  }

  override def printWelcome() {
    echo(s"""
      |Welcome to ChemTuring v3.0!
      | [Using Scala $versionString on $javaVmName, Java $javaVersion]
      |Type in expressions to have them evaluated.
      |Type :help for more information.
      |
      |The following classes are imported automatically:
      |  State
      |  Mode
      |    EXEC, KILL
      |  Operation
      |    READ, WRITE, SKIP, MSKIP
      |
      """.trim.stripMargin
    )
  }

  override def prompt = "==> "
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
