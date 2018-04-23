package com.cartracker.api.dtos

final case class CarTelemetryDto(carId: String,
                                 telemetry: TelemetryDto)