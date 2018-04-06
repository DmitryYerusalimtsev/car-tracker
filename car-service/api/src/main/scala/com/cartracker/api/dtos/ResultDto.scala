package com.cartracker.api.dtos

case class ResultDto(success: Boolean, errorMessage: String) {
  def this() = this(true, "")

  def this(errorMessage: String) = this(false, errorMessage)
}
