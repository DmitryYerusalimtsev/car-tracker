package com.cartracker.api.dtos.extensions

import com.cartracker.api.dtos.{PositionDto, TelemetryDto}
import com.cartracker.carservice.core.Telemetry

object EntityMappingExtensions {

  implicit class ExtendedTelemetry(val value: Telemetry) {
    def toDto = TelemetryDto(value.remainingFuel,
      PositionDto(
        value.currentPosition.longitude,
        value.currentPosition.latitude))
  }

}
