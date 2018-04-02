package com.cartracker.api.dtos

import java.util.UUID

final case class RecordTelemetryDto(
                                     carId: UUID,
                                     remainingFuel: Integer,
                                     currentPosition: PositionDto)