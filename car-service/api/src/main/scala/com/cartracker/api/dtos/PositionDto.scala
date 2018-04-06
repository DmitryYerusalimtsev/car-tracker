package com.cartracker.api.dtos

import com.cartracker.carservice.core.Position

case class PositionDto(longitude: Float, latitude: Float){
  def toEntity: Position = Position(longitude, latitude)
}
