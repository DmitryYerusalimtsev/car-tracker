package com.cartracker.application.helpers

trait TryWithResources {
  def use[A <: AutoCloseable, B](closeable: A)(fun: (A) ⇒ B): B = {
    try {
      fun(closeable)
    } finally {
      closeable.close()
    }
  }
}
