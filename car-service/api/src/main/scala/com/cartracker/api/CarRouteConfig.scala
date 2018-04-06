package com.cartracker.api

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import com.cartracker.api.dtos.{GetTelemetryDto, ResultDto, TelemetryDto}
import org.apache.commons.lang3.exception.ExceptionUtils
import com.cartracker.api.json.CarServiceFormatSupport._
import com.cartracker.application.actors.{Car, CarsManager}
import com.cartracker.application.actors.Car.{ReadTelemetry, RecordTelemetry, RespondTelemetry}
import java.util.UUID

import com.cartracker.api.dtos.extensions.EntityMappingExtensions._
import akka.util.Timeout
import akka.pattern.ask
import com.cartracker.application.actors.CarsManager.{GetCar, RespondCar}

import scala.concurrent.duration._

final class CarRouteConfig(system: ActorSystem) {

  private implicit val executionContext = system.dispatcher
  private val carsManager = system.actorOf(CarsManager.props(), "cars-manager")

  val exceptionHandler = ExceptionHandler {
    case ex: Exception => extractUri { _ =>
      complete(HttpResponse(StatusCodes.InternalServerError, entity = ExceptionUtils.getStackTrace(ex)))
    }
  }

  def route(): Route = {
    pathPrefix("car" / JavaUUID) { id =>
      post {
        entity(as[TelemetryDto]) { dto =>
          val requestId = UUID.randomUUID()
          val resultDto = for {
            carResponse <- getCar(requestId, id.toString);
            _ <- carResponse.car ! RecordTelemetry(requestId, dto.toEntity)
          } yield new ResultDto()
          complete(resultDto)

        } ~ get {
          implicit val timeout: Timeout = 5.seconds
          val requestId = UUID.randomUUID()

          val dto = for (
            carResponse <- getCar(requestId, id.toString);
            dto <- (carResponse.car ? ReadTelemetry(requestId)).mapTo[RespondTelemetry].map(rt => {
              rt.value match {
                case Some(t) => new GetTelemetryDto(t.toDto)
                case None => new GetTelemetryDto("No telemetry information for specified car")
              }
            })
          ) yield dto
          complete(dto)

        } ~ post {
          entity(as[TelemetryDto]) { dto =>
            val car = getActor(id.toString)
            car ! RecordTelemetry(UUID.randomUUID(), dto.toEntity)
            complete(new ResultDto())
          }
        }
      }
    }
  }

  private def getCar(requestId: UUID, carId: String) =
    (carsManager ? GetCar(requestId, carId)).mapTo[RespondCar]
}

object CarRouteConfig {
  def apply(implicit system: ActorSystem): CarRouteConfig = new CarRouteConfig(system)
}
