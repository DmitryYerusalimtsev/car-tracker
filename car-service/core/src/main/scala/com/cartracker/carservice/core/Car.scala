package com.cartracker.carservice.core

import java.util.UUID

final case class Car(
                      id: UUID,
                      name: String,
                      owner: Person
                    )