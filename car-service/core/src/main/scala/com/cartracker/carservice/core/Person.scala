package com.cartracker.carservice.core

import java.util.UUID

final case class Person(
                         id: UUID,
                         firstName: String,
                         lastName: String) {
  val fullName = s"$firstName $lastName"
}
