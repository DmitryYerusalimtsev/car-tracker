package com.cartracker.api.dtos

final case class GetAllTelemetryDto(success: Boolean,
                                    errorMessage: Option[String],
                                    telemetry: List[CarTelemetryDto]) {
  def this(telemetry: Seq[CarTelemetryDto]) = this(true, None, telemetry.toList)

  def this(errorMessage: String) = this(false, Some(errorMessage), List())
}
