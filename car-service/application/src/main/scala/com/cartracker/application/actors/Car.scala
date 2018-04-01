package com.cartracker.application.actors

import java.util.UUID

import akka.actor.{Actor, ActorLogging, Props}
import com.cartracker.carservice.core.Telemetry

class Car(carId: String) extends Actor with ActorLogging {

  import Car._

  var lastTelemetry: Option[Telemetry] = None

  override def preStart(): Unit = log.info("Car actor {} started", carId)

  override def postStop(): Unit = log.info("Car actor {} stopped", carId)

  override def receive: Receive = {
    case ReadTelemetry(id) ⇒
      sender() ! RespondTelemetry(id, lastTelemetry)
  }
}

object Car {
  def props(carId: String): Props = Props(new Car(carId))

  final case class ReadTelemetry(requestId: UUID)

  final case class RespondTelemetry(requestId: UUID, value: Option[Telemetry])

}
