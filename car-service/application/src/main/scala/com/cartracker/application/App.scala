package com.cartracker.application

import akka.actor.ActorSystem
import com.cartracker.application.actors.CarTrackerSupervisor

import scala.io.StdIn

object App {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("car-tracker-system")

    try {
      // Create top level supervisor
      val supervisor = system.actorOf(CarTrackerSupervisor.props(), "cars-supervisor")
      // Exit the system after ENTER is pressed
      StdIn.readLine()
    } finally {
      system.terminate()
    }
  }
}