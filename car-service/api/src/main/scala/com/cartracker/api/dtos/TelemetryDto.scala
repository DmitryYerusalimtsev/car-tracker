package com.cartracker.api.dtos

import java.time.LocalDateTime

import com.cartracker.carservice.core.Telemetry

final case class TelemetryDto(
                               timestamp: String,
                               remainingFuel: Int,
                               currentPosition: PositionDto) {

  def toEntity: Telemetry = Telemetry(
    LocalDateTime.parse(timestamp),
    remainingFuel,
    currentPosition.toEntity)
}
