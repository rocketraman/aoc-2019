package day6

import input.input

fun main() {
  val mapData = input(6/*, "test.txt"*/)
    .map { it.split(')') }

//  println(mapData)

  val orbits = orbits(mapData)

  //println(orbits.asString())
  println(orbits.sumBy { it.path().size })

  println("Done")
}
