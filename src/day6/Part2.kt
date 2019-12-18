package day6

import input.input

fun main() {
  val mapData = input(6/*, "test.txt"*/)
    .map { it.split(')') }

  val orbits = orbits(mapData)

  val YOU = orbits.find { it.value == "YOU" }!!
  val SAN = orbits.find { it.value == "SAN" }!!

  val commonOrbit = YOU.commonAncestor(SAN)!!
  println(commonOrbit.value)

  val pathToYOU = YOU.path().reversed()
  val pathToSAN = SAN.path().reversed()

  val indexOfCA = pathToYOU.indexOf(commonOrbit.value)
  val orbitalTransfersYOUToCommon = pathToYOU.drop(indexOfCA).size - 1
  val orbitalTransfersSANToCommon = pathToSAN.drop(indexOfCA).size - 1

  println(orbitalTransfersYOUToCommon + orbitalTransfersSANToCommon)
  println("Done")
}
