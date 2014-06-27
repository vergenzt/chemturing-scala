package com.timvergenz.chemturing.ui

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.ILoop
import scala.util.Properties.javaVersion
import scala.util.Properties.javaVmName
import scala.util.Properties.versionString

import com.timvergenz.chemturing.core.State
import com.timvergenz.chemturing.util.Util.BinaryStringHelper

class ChemTuringREPL extends ILoop {

  override def createInterpreter() = {
    super.createInterpreter()
    intp.initialize {
      intp.beQuietDuring {
        // initial commands to run for the session
        //intp.bind(NamedParam("_commands", replCommands))
        //intp.interpret("import _commands._")
        intp.interpret("import scala.language.postfixOps")
        intp.interpret("import com.timvergenz.chemturing.core._")
        intp.interpret("import com.timvergenz.chemturing.util.Util._")
        intp.interpret("import com.timvergenz.chemturing.util.StateGenerators._")
        intp.interpret("import com.timvergenz.chemturing.ui.ChemTuringREPLRunner.repl.replCommands._")
      }
    }
  }

  /**
   * State modification stack (keeps everything undoable)
   */
  private var _statesStack: List[State] = State(seq"0000000000") :: Nil
  def state =
    _statesStack.head
  def state_=(newState: State) =
    _statesStack ::= newState
  def pop() = {
    _statesStack = _statesStack.tail
  }

  /**
   * Commands
   */
  val replCommands = new Commands
  class Commands {
    def print = {
      println(state)
    }
    def next = {
      state = state.next
      print
    }
    def back = {
      pop()
      print
    }
    def set(newState: State) = {
      state = newState
      print
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

  val settings = new Settings
  settings.usejavacp.value = true
  settings.deprecation.value = true
  settings.nopredef.value = true

  val repl = new ChemTuringREPL()

  def main(args: Array[String]) {
    repl.process(settings)
  }
}
