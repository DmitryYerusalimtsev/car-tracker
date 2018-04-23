package com.cartracker.api

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.cartracker.api.config.Settings
import com.cartracker.api.routes.MainRouter
import org.apache.ignite.Ignition

import scala.concurrent.{Await, Promise}
import scala.concurrent.duration.Duration

object WebServer {
  def main(args: Array[String]) {

    implicit val system = ActorSystem("CarTracker")
    implicit val materializer = ActorMaterializer()
    implicit val ignite = Ignition.start()

    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val clusterController = system.actorOf(ClusterController.props(), "clusterController")

    val serverFuture = for {_ <- Http().bindAndHandle(MainRouter(system).routes, Settings.Http.host, Settings.Http.port)
                            waitOnFuture <- Promise[Done].future}
      yield waitOnFuture

    println(s"Server online at http://${Settings.Http.host}:${Settings.Http.port}/")

    // Cleanup before shutdown
    sys.addShutdownHook {
      ignite.close()
    }

    Await.ready(serverFuture, Duration.Inf)
  }
}
