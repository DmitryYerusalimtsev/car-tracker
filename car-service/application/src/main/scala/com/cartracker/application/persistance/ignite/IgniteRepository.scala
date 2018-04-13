package com.cartracker.application.persistance.ignite

import org.apache.ignite.{Ignite, Ignition}

trait IgniteRepository {
  def igniteFunc[T](func: Ignite => T): T = {
    val ignite: Ignite = Ignition.start()
    try {
      func(ignite)
    }
    finally {
      ignite.close()
    }
  }
}
