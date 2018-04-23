package com.cartracker.carservice.core

import java.time.LocalDateTime

case class Telemetry(
                      timestamp: LocalDateTime,
                      remainingFuel: Int,
                      currentPosition: Position
                    ) {
  def this() = this(LocalDateTime.now(), 0, Position(0, 0))
}