package com.timvergenz.chemturing.server

import scala.Left
import scala.Right

import com.timvergenz.chemturing.core.State
import com.timvergenz.chemturing.util.UrlEncoded

import akka.actor.Actor
import spray.httpx.unmarshalling.Deserializer
import spray.httpx.unmarshalling.MalformedContent
import spray.routing.ConjunctionMagnet.fromDirective
import spray.routing.Directive.pimpApply
import spray.routing.HttpService
import spray.routing.directives.ParamDefMagnet.apply

class ChemTuringServiceActor extends Actor with ChemTuringService {
  def actorRefFactory = context
  def receive = runRoute(route)
}

trait ChemTuringService extends HttpService {

  implicit object StateDeserializer extends Deserializer[String, State] {
    def apply(stateString: String) = stateString match {
      case UrlEncoded(state) => Right(state)
      case _ => Left(MalformedContent("Could not extract state"))
    }
  }

  val route = {
    (path("successor") & parameter('state.as[State])) { state =>
      complete {
        UrlEncoded(state.next)
      }
    }
  }

}
