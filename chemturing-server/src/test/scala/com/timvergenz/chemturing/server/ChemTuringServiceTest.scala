package com.timvergenz.chemturing.server

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._

class ChemTuringServiceTest extends Specification with Specs2RouteTest with ChemTuringService {
  def actorRefFactory = system

  /*
  "ChemTuringService" should {

    "return a greeting for GET requests to the root path" in {
      Get() ~> route ~> check {
        responseAs[String] must contain("Say hello")
      }
    }

    "leave GET requests to other paths unhandled" in {
      Get("/kermit") ~> myRoute ~> check {
        handled must beFalse
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put() ~> sealRoute(myRoute) ~> check {
        status === MethodNotAllowed
        responseAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }
  }
  */
}
