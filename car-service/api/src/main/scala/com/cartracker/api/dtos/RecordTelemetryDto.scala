package com.cartracker.api.dtos

import java.util.UUID

import com.cartracker.carservice.core.Telemetry

final case class RecordTelemetryDto(
                                     carId: UUID,
                                     remainingFuel: Int,
                                     currentPosition: PositionDto) {
  def toEntity: Telemetry = Telemetry(remainingFuel, currentPosition.toEntity)
}