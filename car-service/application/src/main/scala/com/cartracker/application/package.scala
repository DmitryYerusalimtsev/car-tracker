package com.cartracker

package object application {
  type MutableMap[A, B] = scala.collection.mutable.HashMap[A, B]
}
