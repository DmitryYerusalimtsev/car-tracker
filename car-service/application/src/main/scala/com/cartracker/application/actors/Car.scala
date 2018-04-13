package com.cartracker.application.actors

import java.util.UUID

import akka.actor.{Actor, ActorLogging, Props}
import com.cartracker.application.persistance.TelemetryRepository
import com.cartracker.carservice.core.Telemetry

final class Car(carId: String, telemetryRep: TelemetryRepository) extends Actor with ActorLogging {

  import Car._

  override def preStart(): Unit = log.info("Car actor {} started", carId)

  override def postStop(): Unit = log.info("Car actor {} stopped", carId)

  override def receive: Receive = {
    case RecordTelemetry(id, value) =>
      log.info("Recorded telemetry reading {} with {}", value, id)
      telemetryRep.saveTelemetry(carId, value)
      sender() ! TelemetryRecorded(id)

    case ReadTelemetry(id) =>
      val lastTelemetry = telemetryRep.getTelemetry(carId)
      sender() ! RespondTelemetry(id, lastTelemetry)
  }
}

object Car {
  def props(carId: String, telemetryRep: TelemetryRepository): Props = Props(new Car(carId, telemetryRep))

  final case class RecordTelemetry(requestId: UUID, value: Telemetry)
  final case class TelemetryRecorded(requestId: UUID)

  final case class ReadTelemetry(requestId: UUID)
  final case class RespondTelemetry(requestId: UUID, value: Option[Telemetry])
}
