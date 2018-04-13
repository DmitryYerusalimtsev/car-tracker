package com.cartracker.application.persistance

import com.cartracker.carservice.core.Telemetry

trait TelemetryRepository {
  def saveTelemetry(carId: String, telemetry: Telemetry): Unit

  def getTelemetry(carId: String): Option[Telemetry]
}
