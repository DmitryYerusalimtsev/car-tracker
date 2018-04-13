package com.cartracker.application.persistance.ignite

import com.cartracker.application.persistance.TelemetryRepository
import com.cartracker.carservice.core.Telemetry
import org.apache.ignite.{Ignite, IgniteCache}

final class IgniteTelemetryRepository(ignite: Ignite) extends TelemetryRepository {
  private val cacheName = "telemetry"

  override def saveTelemetry(carId: String, telemetry: Telemetry): Unit = {
    val cache: IgniteCache[String, Telemetry] = ignite.getOrCreateCache(cacheName)
    cache.put(getCacheKey(carId), telemetry)
  }

  override def getTelemetry(carId: String): Option[Telemetry] = {
    val cache: IgniteCache[String, Telemetry] = ignite.getOrCreateCache(cacheName)
    val telemetry = cache.get(getCacheKey(carId))
    Option(telemetry)
  }

  private def getCacheKey(carId: String) = s"car-$carId"
}

object IgniteTelemetryRepository {
  def apply(ignite: Ignite): IgniteTelemetryRepository = new IgniteTelemetryRepository(ignite)
}
