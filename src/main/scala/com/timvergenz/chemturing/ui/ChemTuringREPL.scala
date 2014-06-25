package com.timvergenz.chemturing.ui

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.ILoop
import scala.util.Properties.javaVersion
import scala.util.Properties.javaVmName
import scala.util.Properties.versionString

import com.timvergenz.chemturing.core.State

object ChemTuringREPL {
  var state: State = _

  def print = println(state)
  def next = { state = state.next; print }
}

class ChemTuringILoop extends ILoop {
  override def createInterpreter() = {
    super.createInterpreter()
    intp.initialize {
      intp.beQuietDuring {
        // initial commands to run for the session
        intp.interpret("import com.timvergenz.chemturing.core._")
        intp.interpret("import com.timvergenz.chemturing.util._")
        intp.interpret("import com.timvergenz.chemturing.util.StateGenerators._")
        intp.interpret("import com.timvergenz.chemturing.util.Util._")
        intp.interpret("import com.timvergenz.chemturing.ui.ChemTuringREPL._")
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
      """.trim.stripMargin)
  }

  override def prompt = "==> "
}

object ChemTuringREPLRunner {

  def main(args: Array[String]) {

    val settings = new Settings
    settings.usejavacp.value = true
    settings.deprecation.value = true
    settings.nopredef.value = true
    new ChemTuringILoop().process(settings)
  }
}
