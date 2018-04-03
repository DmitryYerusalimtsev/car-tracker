package com.cartracker.application

import akka.actor.ActorSystem
import com.cartracker.application.actors.ClusterController

object Main extends App {
  val system = ActorSystem("CarTracker")
  val clusterController = system.actorOf(ClusterController.props(), "clusterController")
}
