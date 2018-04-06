package com.cartracker.api.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.cartracker.api.dtos.{PositionDto, RecordTelemetryDto, ResultDto}
import spray.json.DefaultJsonProtocol
import UUIDMarshalling._

  object CarServiceFormatSupport extends DefaultJsonProtocol with SprayJsonSupport {
    implicit val positionFormat = jsonFormat2(PositionDto)
    implicit val recordTelemetryFormat = jsonFormat3(RecordTelemetryDto)
    implicit val resultFormat = jsonFormat2(ResultDto)
}
