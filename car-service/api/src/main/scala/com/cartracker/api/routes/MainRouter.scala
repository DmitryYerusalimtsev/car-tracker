package com.cartracker.api.routes

import java.util.Calendar

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

final class MainRouter(system: ActorSystem) {
  val routes = defaultRoutes() ~ CarRouteConfig(system).route()

  private def defaultRoutes(): Route = {
    get {
      path("healthcheck") {
        complete(Calendar.getInstance().getTime.toString)
      }
    }
  }
}

object MainRouter {
  def apply(implicit system: ActorSystem): MainRouter = new MainRouter(system)
}
