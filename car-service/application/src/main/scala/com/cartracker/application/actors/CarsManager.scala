package com.cartracker.application.actors

import java.util.UUID

import akka.actor.Status.Failure
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.cartracker.application.MutableMap
import com.cartracker.application.persistance.TelemetryRepository
import com.cartracker.carservice.core.Telemetry

final class CarsManager(telemetryRep: TelemetryRepository) extends Actor with ActorLogging {

  import CarsManager._

  private val carIdToActor = new MutableMap[String, ActorRef].empty

  override def receive: Receive = {
    case RequestTrackingCar(id, carId) =>
      log.info("Registering a car with id {}", carId)

      carIdToActor.get(carId) match {
        case Some(_) => CarTracking(id)
        case None =>
          log.info("Creating car actor for {}", carId)
          val carActor = context.actorOf(Car.props(carId, telemetryRep), s"car-$carId")
          context.watch(carActor)
          carIdToActor += carId -> carActor
          sender() ! CarTracking(id)
      }

    case GetCar(id, carId) =>
      log.info("Retrieving a car with id {}", carId)
      carIdToActor.get(carId) match {
        case Some(actor) => sender() ! RespondCar(id, actor)
        case None => sender() ! Failure
      }

    case GetAllCarTelemetry(id) =>
      log.info("Retrieving all car telemetry")
      val result = telemetryRep.getAllCarTelemetry(carIdToActor.keySet.toSeq)
      sender() ! AllCarTelemetry(id, result)
  }
}

object CarsManager {
  def props(telemetryRep: TelemetryRepository): Props = Props(new CarsManager(telemetryRep))

  final case class RequestTrackingCar(requestId: UUID, carId: String)

  final case class CarTracking(requestId: UUID)

  final case class GetCar(requestId: UUID, carId: String)

  final case class RespondCar(requestId: UUID, car: ActorRef)

  final case class GetAllCarTelemetry(requestId: UUID)

  final case class AllCarTelemetry(requestId: UUID, telemetryMap: Map[String, Option[Telemetry]])

}