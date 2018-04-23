package com.cartracker.application.mocks

import java.time.LocalDateTime

import com.cartracker.application.persistance.TelemetryRepository
import com.cartracker.carservice.core.{Position, Telemetry}

class MockTelemetryRepository extends TelemetryRepository {
  override def saveTelemetry(carId: String, telemetry: Telemetry): Unit = {}

  override def getTelemetry(carId: String): Option[Telemetry] = {
    Some(Telemetry(LocalDateTime.now(), 1, Position(1, 1)))
  }

  override def getAllCarTelemetry(carIds: Seq[String]): Map[String, Option[Telemetry]] = Map()
}
