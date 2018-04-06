package com.cartracker.api.dtos

import com.cartracker.carservice.core.Telemetry

final case class TelemetryDto(
                               remainingFuel: Int,
                               currentPosition: PositionDto) {
  def toEntity: Telemetry = Telemetry(remainingFuel, currentPosition.toEntity)
}
