package com.timvergenz.chemturing.ui

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.ILoop
import scala.util.Properties.javaVersion
import scala.util.Properties.javaVmName
import scala.util.Properties.jdkHome
import scala.util.Properties.versionString
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import scala.reflect.ClassTag
import akka.actor.FSM
import akka.actor.Actor
import com.timvergenz.chemturing.core.State
import akka.actor.FSM.Event

class ChemTuringActor extends Actor {
  import ChemTuringREPL._

  var state: State = _

  def receive = {
    case setstate(next) => state = next
    case step(n) => state = state.next(n)
    case print => println(state.toString)
  }
}

object ChemTuringREPL {
  implicit val system = ActorSystem("replActorSystem")
  implicit val dispatcher = system.dispatcher
  implicit val replActor = system.actorOf(Props[ChemTuringActor], "repl")
  implicit val timeout = Timeout(5.seconds)

  sealed trait REPLCommand {
    val future = (replActor ? this)
    future onSuccess {
      case reply: String => println(reply)
    }
    future onFailure {
      case e: Exception => e.printStackTrace()
    }
    Await.result(future, timeout.duration)
  }
  case class setstate(val next: State) extends REPLCommand
  case class step(val n: Int = 1) extends REPLCommand
  case class print() extends REPLCommand
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
