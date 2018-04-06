package com.cartracker.api

import java.util.UUID
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.pattern.ask
import akka.util.Timeout
import com.cartracker.api.dtos.extensions.EntityMappingExtensions._
import com.cartracker.api.dtos.{GetTelemetryDto, ResultDto, TelemetryDto}
import com.cartracker.api.json.CarServiceFormatSupport._
import com.cartracker.application.actors.Car.{ReadTelemetry, RecordTelemetry, RespondTelemetry}
import com.cartracker.application.actors.CarsManager
import com.cartracker.application.actors.CarsManager.{GetCar, RespondCar}
import org.apache.commons.lang3.exception.ExceptionUtils
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
            carResponse <- getCar(requestId, id.toString)
            _ <- carResponse.car ! RecordTelemetry(requestId, dto.toEntity)
          } yield new ResultDto()
          complete(resultDto)

        } ~ get {
          implicit val timeout: Timeout = 5.seconds
          val requestId = UUID.randomUUID()

          val result = for (
            carResponse <- getCar(requestId, id.toString);
            dto <- (carResponse.car ? ReadTelemetry(requestId)).mapTo[RespondTelemetry].map(rt => {
              rt.value match {
                case Some(t) => new GetTelemetryDto(t.toDto)
                case None => new GetTelemetryDto("No telemetry information for specified car")
              }
            })
          ) yield dto
          complete(result)

        } ~ post {
          entity(as[TelemetryDto]) { dto =>
            val requestId = UUID.randomUUID()
            val result = for (
              carResponse <- getCar(requestId, id.toString);
              _ <- carResponse.car ! RecordTelemetry(requestId, dto.toEntity)
            ) yield new ResultDto()
            complete(result)
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
