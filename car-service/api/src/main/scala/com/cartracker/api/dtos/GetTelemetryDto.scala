package com.cartracker.api.dtos

final case class GetTelemetryDto(success: Boolean,
                                 errorMessage: Option[String],
                                 telemetry: Option[TelemetryDto]) {
  def this(telemetry: TelemetryDto) = this(true, None, Some(telemetry))

  def this(errorMessage: String) = this(false, Some(errorMessage), None)
}
