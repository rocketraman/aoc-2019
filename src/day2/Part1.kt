package day2

import input.input

fun main() {
  val memory = input(2/*, "test.txt"*/).first().split(',').map { it.toInt() }.toTypedArray().toIntArray()
  val result = runProgram(memory, 12, 2)

  println("Result=$result")
  println("Done")
}
