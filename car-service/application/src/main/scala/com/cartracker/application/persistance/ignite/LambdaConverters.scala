package com.cartracker.application.persistance.ignite

import org.apache.ignite.lang.IgniteBiPredicate

object LambdaConverters {
  implicit def toIgniteBiPredicate[K, V](func: (K, V) => Boolean) =
    new IgniteBiPredicate[K, V] {
      override def apply(k: K, v: V): Boolean = func(k, v)
    }
}
