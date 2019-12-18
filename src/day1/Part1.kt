package day1

import input.input

fun main() {
  val input = input(1)
  val mass = input
    .map { it.toInt() }
    .map { it / 3 }
    .map { it - 2 }
    .sum()
  println("Mass = $mass")
}
