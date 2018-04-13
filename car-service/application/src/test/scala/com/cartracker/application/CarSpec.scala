package com.cartracker.application

import java.util.UUID

import akka.actor.ActorSystem
import akka.testkit.TestProbe
import com.cartracker.application.actors.Car
import com.cartracker.application.mocks.MockTelemetryRepository
import com.cartracker.carservice.core.{Position, Telemetry}
import org.scalatest._

class CarSpec extends FunSpecLike with Matchers with BeforeAndAfterEach {
  implicit val system = ActorSystem()

  val rep = new MockTelemetryRepository()

  describe("Car Actor") {

    describe("given RespondTelemetry message") {

      val probe = TestProbe()
      val deviceActor = system.actorOf(Car.props("car", rep))
      val requestId = UUID.randomUUID()

      val telemetry = Telemetry(10, Position(1, 1))

      it("should reply with empty reading if no telemetry is known") {
        deviceActor.tell(Car.ReadTelemetry(requestId), probe.ref)
        val response = probe.expectMsgType[Car.RespondTelemetry]
        response.requestId should ===(requestId)
        response.value should ===(None)
      }

      it("should reply with success when telemetry recorded") {
        deviceActor.tell(Car.RecordTelemetry(requestId, telemetry), probe.ref)
        val response = probe.expectMsgType[Car.TelemetryRecorded]
        response.requestId should ===(requestId)
      }

      it("should reply with correct telemetry when telemetry recorded") {
        val telemetry = Telemetry(10, Position(1, 1))

        val probe2 = TestProbe()

        deviceActor.tell(Car.RecordTelemetry(requestId, telemetry), probe.ref)
        deviceActor.tell(Car.ReadTelemetry(requestId), probe2.ref)

        val response = probe2.expectMsgType[Car.RespondTelemetry]

        response.requestId should ===(requestId)
        response.value should ===(Some(telemetry))
      }
    }
  }
}
