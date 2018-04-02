package com.cartracker.api.config

import com.typesafe.config.ConfigFactory

object Settings {
  private val config = ConfigFactory.load()

  object Http {
    private val service = config.getConfig("http")

    lazy val host = service.getString("host")
    lazy val port = service.getInt("port")
  }
}
