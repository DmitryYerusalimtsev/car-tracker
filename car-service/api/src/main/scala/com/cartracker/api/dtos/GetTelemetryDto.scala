package com.cartracker.api.dtos

final case class GetTelemetryDto(success: Boolean,
                                 errorMessage: String,
                                 telemetry: TelemetryDto) {
  def this(telemetry: TelemetryDto) = this(true, "", telemetry)

  def this(errorMessage: String) = this(false, errorMessage, null)
}
