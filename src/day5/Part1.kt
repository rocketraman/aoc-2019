package day5

import input.input

fun main() {
  val memory = input(5/*, "test.txt"*/).first().split(',').map { it.toInt() }.toTypedArray().toIntArray()
  runProgram(memory, { 1 }, { println(it) })

  println("Done")
}
