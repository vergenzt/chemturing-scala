package com.timvergenz.chemturing.server

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

object Boot extends App {
  implicit val system = ActorSystem("chemturing-actor-system")

  // create and start our service actor
  val service = system.actorOf(Props[ChemTuringServiceActor], "chemturing-service")

  implicit val timeout = Timeout(5.seconds)
  IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)
}
