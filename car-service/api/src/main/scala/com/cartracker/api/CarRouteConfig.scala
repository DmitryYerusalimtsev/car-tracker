package com.cartracker.api

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import com.cartracker.api.dtos.{RecordTelemetryDto, ResultDto}
import org.apache.commons.lang3.exception.ExceptionUtils
import com.cartracker.api.json.CarServiceFormatSupport._
import com.cartracker.application.actors.Car
import com.cartracker.application.actors.Car.RecordTelemetry
import java.util.UUID

final class CarRouteConfig(system: ActorSystem) {
  val exceptionHandler = ExceptionHandler {
    case ex: Exception => extractUri { _ =>
      complete(HttpResponse(StatusCodes.InternalServerError, entity = ExceptionUtils.getStackTrace(ex)))
    }
  }

  def route(): Route = {
    post {
      path("car" / "recordtelemetry") {
        entity(as[RecordTelemetryDto]) { dto =>
          val car = getActor(dto.carId.toString)
          car ! RecordTelemetry(UUID.randomUUID(), dto.toEntity)
          complete(new ResultDto())
        }
      }
    }
  }

  private def getActor(carId: String) = system.actorOf(Car.props(carId), carId)
}

object CarRouteConfig {
  def apply(implicit system: ActorSystem): CarRouteConfig = new CarRouteConfig(system)
}
