package day2

import input.input

fun main() {
  val memory = input(2/*, "test.txt"*/).first().split(',').map { it.toInt() }.toTypedArray().toIntArray()

  val targetValue = 19_690_720

  // we're gonna brute-force it, would be interested in algorithmic solutions instead
  (0..99).forEach { noun ->
    (0..99).forEach { verb ->
      if(runProgram(memory.clone(), noun, verb) == targetValue) {
        println("Found result noun=$noun, verb=$verb, result=${100 * noun + verb}")
      }
    }
  }

  println("Done")
}
