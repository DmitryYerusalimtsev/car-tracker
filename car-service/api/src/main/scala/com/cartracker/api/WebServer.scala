package com.cartracker.api

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.cartracker.api.config.Settings

import scala.concurrent.{Await, Promise}
import scala.concurrent.duration.Duration

object WebServer {
  def main(args: Array[String]) {

    implicit val system = ActorSystem("car-service-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val serverFuture = for {_ <- Http().bindAndHandle(MainRouter.routes, Settings.Http.host, Settings.Http.port)
                            waitOnFuture <- Promise[Done].future}
      yield waitOnFuture

    println(s"Server online at http://${Settings.Http.host}:${Settings.Http.port}/")

    Await.ready(serverFuture, Duration.Inf)
  }
}
