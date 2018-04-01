package com.cartracker.application.actors

import akka.actor.{Actor, ActorLogging, Props}

class CarTrackerSupervisor extends Actor with ActorLogging {
  override def preStart(): Unit = log.info("CarsTracker Application started")

  override def postStop(): Unit = log.info("CarsTracker Application stopped")

  // No need to handle any messages
  override def receive = Actor.emptyBehavior
}

object CarTrackerSupervisor {
  def props(): Props = Props(new CarTrackerSupervisor)
}