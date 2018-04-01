package com.cartracker.carservice.core

case class Telemetry(
                      remainingFuel: Integer,
                      currentPosition: Position
                    )

case class Position(longitude: Float, latitude: Float)