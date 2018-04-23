package com.cartracker.api.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.cartracker.api.dtos._
import spray.json.DefaultJsonProtocol

object CarServiceFormatSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val positionFormat = jsonFormat2(PositionDto)
  implicit val telemetryFormat = jsonFormat3(TelemetryDto)
  implicit val resultFormat = jsonFormat2(ResultDto)
  implicit val getTelemetryFormat = jsonFormat3(GetTelemetryDto)
  implicit val carTelemetryFormat = jsonFormat2(CarTelemetryDto)
  implicit val getAllTelemetryFormat = jsonFormat3(GetAllTelemetryDto)
}
