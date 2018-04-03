package com.cartracker.api

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import com.cartracker.api.dtos.RecordTelemetryDto
import org.apache.commons.lang3.exception.ExceptionUtils
import com.cartracker.api.json.CarServiceFormatSupport._

object CarRouteConfig {
  val exceptionHandler = ExceptionHandler {
    case ex: Exception => extractUri { _ =>
      complete(HttpResponse(StatusCodes.InternalServerError, entity = ExceptionUtils.getStackTrace(ex)))
    }
  }

  def route(): Route = {
    path("car") {
      post {
        path("recordtelemetry") {
          entity(as[RecordTelemetryDto]) { dto =>

          }
        }
      }
    }
  }
}
