package day3

import input.input

fun main() {
  val input = input(1/*, file = "test.txt"*/)

  val weightOfFuel = 1

  fun fuelForMass(mass: Int): Int = mass / 3 - 2

  fun fuelWeight(fuel: Int): Int = fuelForMass(fuel * weightOfFuel)

  val mass = input
    .map { it.toInt() }
    .map(::fuelForMass)
    .flatMap {
      var fuel = it
      var fuelWeight = fuelWeight(fuel)
      sequence {
        yield(fuel)
        while(fuelWeight > 0) {
          yield(fuelWeight)
          fuel = fuelWeight / weightOfFuel
          fuelWeight = fuelWeight(fuel)
        }
      }.toList()
    }
    .sum()

  println("Mass = $mass")
}
