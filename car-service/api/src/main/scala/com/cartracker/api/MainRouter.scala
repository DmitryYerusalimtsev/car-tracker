package com.cartracker.api

import java.util.Calendar

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object MainRouter {
  val routes = defaultRoutes() ~ CarRouteConfig.route()

  private def defaultRoutes(): Route = {
    get {
      path("healthcheck") {
        complete(Calendar.getInstance().getTime.toString)
      }
    }
  }
}
