package day7

import input.input

suspend fun main() {
  val amplifierController = input(7/*, "test.txt"*/).first().split(',').map { it.toInt() }.toTypedArray().toIntArray()

  suspend fun amplify(phases: List<Int>): Int {
    var result = 0
    runProgram(amplifierController.copyOf(), { i -> if (i == 0) phases[0] else 0 }) { outputA ->
      runProgram(amplifierController.copyOf(), { i -> if (i == 0) phases[1] else outputA }) { outputB ->
        runProgram(amplifierController.copyOf(), { i -> if (i == 0) phases[2] else outputB }) { outputC ->
          runProgram(amplifierController.copyOf(), { i -> if (i == 0) phases[3] else outputC }) { outputD ->
            runProgram(amplifierController.copyOf(), { i -> if (i == 0) phases[4] else outputD }) { outputE ->
              result = outputE
            }
          }
        }
      }
    }
    return result
  }

  val thrusterSettings = permute((0..4).toList()).map {
    it to amplify(it)
  }

  //println(thrusterSettings)
  val best = thrusterSettings.maxBy { it.second }

  println(best)
  println("Done")
}
