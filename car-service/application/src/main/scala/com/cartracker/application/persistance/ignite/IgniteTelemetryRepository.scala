package com.cartracker.application.persistance.ignite

import com.cartracker.application.MutableMap
import com.cartracker.application.helpers.TryWithResources
import com.cartracker.application.persistance.TelemetryRepository
import com.cartracker.carservice.core.Telemetry
import org.apache.ignite.cache.query.ScanQuery
import org.apache.ignite.{IgniteCache, Ignition}
import LambdaConverters._
import scala.compat.java8.FunctionConverters._

trait IgniteTelemetryRepository extends TelemetryRepository with TryWithResources {

  private val ignite = Ignition.ignite()
  private val cache: IgniteCache[String, Telemetry] = ignite.getOrCreateCache("telemetry")

  override def saveTelemetry(carId: String, telemetry: Telemetry): Unit = {
    cache.put(getCacheKey(carId), telemetry)
  }

  override def getTelemetry(carId: String): Option[Telemetry] = {
    val telemetry = cache.get(getCacheKey(carId))
    Option(telemetry)
  }

  override def getAllCarTelemetry(carIds: Seq[String]): Map[String, Option[Telemetry]] = {
    val query = new ScanQuery[String, Telemetry]((k: String, _: Telemetry) => carIds.contains(getIdFromKey(k)))
    use(cache.query(query)) { cursor =>
      val result = new MutableMap[String, Option[Telemetry]].empty
      cursor.forEach(asJavaConsumer(e => result += getIdFromKey(e.getKey) -> Option(e.getValue)))
      result.toMap
    }
  }

  private def getCacheKey(carId: String) = s"car-$carId"

  private def getIdFromKey(key: String) = key.substring(4)
}

object IgniteTelemetryRepository {
  def apply(): IgniteTelemetryRepository = new IgniteTelemetryRepository {}
}
