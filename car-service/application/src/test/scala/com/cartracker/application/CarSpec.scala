package com.cartracker.application

import java.util.UUID

import akka.actor.ActorSystem
import akka.testkit.TestProbe
import com.cartracker.application.actors.Car
import org.scalatest._

class CarSpec extends FunSpecLike with Matchers with BeforeAndAfterEach {
  implicit val system = ActorSystem()

  describe("car") {

    describe("given RespondTelemetry message") {
      it("should reply with empty reading if no telemetry is known") {
        val probe = TestProbe()
        val deviceActor = system.actorOf(Car.props("car"))

        val requestId = UUID.randomUUID()

        deviceActor.tell(Car.ReadTelemetry(requestId), probe.ref)
        val response = probe.expectMsgType[Car.RespondTelemetry]
        response.requestId should ===(requestId)
        response.value should ===(None)
      }
    }
  }
}
