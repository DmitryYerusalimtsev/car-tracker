package com.cartracker.api

import java.util.Calendar

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.apache.ignite.Ignite

final class MainRouter(system: ActorSystem, ignite: Ignite) {
  val routes = defaultRoutes() ~ CarRouteConfig(system, ignite).route()

  private def defaultRoutes(): Route = {
    get {
      path("healthcheck") {
        complete(Calendar.getInstance().getTime.toString)
      }
    }
  }
}

object MainRouter {
  def apply(implicit system: ActorSystem, ignite: Ignite): MainRouter = new MainRouter(system, ignite)
}
