package com.cartracker.application.mocks

import com.cartracker.application.persistance.TelemetryRepository
import com.cartracker.carservice.core.{Position, Telemetry}

class MockTelemetryRepository extends TelemetryRepository {
  override def saveTelemetry(carId: String, telemetry: Telemetry): Unit = {}

  override def getTelemetry(carId: String): Option[Telemetry] = {
    Some(Telemetry(1, Position(1, 1)))
  }
}
