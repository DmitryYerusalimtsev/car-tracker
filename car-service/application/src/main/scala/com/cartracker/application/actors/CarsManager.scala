package com.cartracker.application.actors

import java.util.UUID

import akka.actor.Status.Failure
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.cartracker.application.MutableMap

class CarsManager extends Actor with ActorLogging {

  import CarsManager._

  private val carIdToActor = new MutableMap[String, ActorRef].empty

  override def receive: Receive = {
    case RequestTrackingCar(id, carId) =>
      log.info("Registering a car with id {}", carId)

      carIdToActor.get(carId) match {
        case Some(_) => CarTracking(id)
        case None =>
          log.info("Creating car actor for {}", carId)
          val carActor = context.actorOf(Car.props(carId), s"car-${carId}")
          context.watch(carActor)
          carIdToActor += carId -> carActor
          sender() ! CarTracking(id)
      }

    case GetCar(id, carId) =>
      log.info("Retrieving a car with id {}", carId)
      carIdToActor.get(carId) match {
        case Some(actor) => RespondCar(id, actor)
        case None => Failure
      }
  }
}

object CarsManager {
  def props(): Props = Props(new CarsManager)

  final case class RequestTrackingCar(requestId: UUID, carId: String)
  final case class CarTracking(requestId: UUID)

  final case class GetCar(requestId: UUID, carId: String)
  final case class RespondCar(requestId: UUID, car: ActorRef)
}