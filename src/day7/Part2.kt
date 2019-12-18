package day7

import input.input
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
suspend fun main() {
  val amplifierController = input(7/*, "test.txt"*/).first().split(',').map { it.toInt() }.toTypedArray().toIntArray()

  data class Amplifier(
    val computer: IntCodeComputer,
    val input: Channel<Int>,
    val output: Channel<Int>
  )

  val ampCount = 5

  suspend fun amplify(phases: List<Int>): Int {
    var result = 0

    coroutineScope {
      // we seem to need this code to run in a single thread, otherwise we get indeterministic results -- not exactly sure why
      // as the computers should just do their own thing until they need input, at which point they'll wait until they get it
      withContext(Executors.newSingleThreadExecutor().asCoroutineDispatcher()) {
        val amplifiers = (0 until ampCount).fold(emptyList<Amplifier>()) { amps, index ->
          val input = if(index == 0) Channel() else amps.last().output
          val output = Channel<Int>()

          amps + Amplifier(
            IntCodeComputer(amplifierController.copyOf(), { input.receive() }) { o -> output.send(o) },
            input,
            output
          )
        }

        amplifiers.forEachIndexed { index, amp ->
          launch {
            println("Amp $index: running")
            amp.computer.runUntilHalt()
            println("Amp $index: halted, closing input channel")
            amp.input.close()
            println("Amp $index: Computer DONE")
          }
        }

        // system inputs
        amplifiers.forEachIndexed { index, amp ->
          launch {
            println("Amp $index: sending phase ${phases[index]}")
            amp.input.send(phases[index])
            if(index == 0) {
              println("Amp $index: sending initial value 0")
              amp.input.send(0)
            }
            println("Amp $index: System Inputs DONE")
          }
        }

        // system outputs
        launch {
          val amp = amplifiers.last()
          val index = ampCount - 1
          amp.output.consumeEach {
            println("Amp $index: consuming output $it")
            when {
              !amplifiers[0].input.isClosedForReceive -> {
                println("Amp $index: feedback output to amp 0: $it")
                amplifiers[0].input.send(it)
              }
              else -> {
                println("Amp $index: result = $it")
                result = it
                amp.output.close()
              }
            }
          }
          println("Amp $index: System Outputs DONE")
        }
      }
    }

    println("RETURN result=$result")
    return result
  }

  val thrusterSettings = coroutineScope {
    permute((5..9).toList()).map {
      it to async { amplify(it) }
    }.map { it.first to it.second.await() }.toMap()
  }

  val best = thrusterSettings.maxBy { it.value }

  println(best)
  println("Done")
}
