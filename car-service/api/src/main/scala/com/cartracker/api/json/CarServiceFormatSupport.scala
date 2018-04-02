package com.cartracker.api.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.cartracker.api.dtos.RecordTelemetryDto
import spray.json.DefaultJsonProtocol

object CarServiceFormatSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val recordTelemetryFormat = jsonFormat3(RecordTelemetryDto)
}
