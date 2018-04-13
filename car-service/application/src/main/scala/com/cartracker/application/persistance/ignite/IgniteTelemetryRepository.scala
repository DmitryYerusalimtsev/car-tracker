package com.cartracker.application.persistance.ignite

import com.cartracker.application.persistance.TelemetryRepository
import com.cartracker.carservice.core.Telemetry
import org.apache.ignite.IgniteCache

trait IgniteTelemetryRepository extends TelemetryRepository with IgniteRepository {
  private val cacheName = "telemetry"

  override def saveTelemetry(carId: String, telemetry: Telemetry): Unit = {
    igniteFunc { ignite =>
      val cache: IgniteCache[String, Telemetry] = ignite.getOrCreateCache(cacheName)
      cache.put(getCacheKey(carId), telemetry)
    }
  }

  override def getTelemetry(carId: String): Option[Telemetry] = {
    igniteFunc { ignite =>
      val cache: IgniteCache[String, Telemetry] = ignite.getOrCreateCache(cacheName)
      val telemetry = cache.get(getCacheKey(carId))
      Option(telemetry)
    }
  }

  private def getCacheKey(carId: String) = s"car-$carId"
}
