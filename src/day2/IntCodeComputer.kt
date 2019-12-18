package day2

import java.lang.Exception

class HaltException: Exception()
class IntCodeComputer(val memory: IntArray) {
  enum class Instruction(
    val opCode: Int,
    val params: (mem: IntArray, instructionPointer: Int) -> IntArray,
    val fn: (mem: IntArray, params: IntArray) -> Int,
    val output: (mem: IntArray, params: IntArray, result: Int) -> Unit) {

    ADD(1,
      { mem, instructionPointer -> intArrayOf(mem[instructionPointer + 1], mem[instructionPointer + 2], mem[instructionPointer + 3]) },
      { mem, params -> mem[params[0]] + mem[params[1]] },
      { mem, params, result -> mem[params[2]] = result }
    ),
    MULTIPLY(2,
      { mem, instructionPointer ->  intArrayOf(mem[instructionPointer + 1], mem[instructionPointer + 2], mem[instructionPointer + 3]) },
      { mem, params -> mem[params[0]] * mem[params[1]] },
      { mem, params, result -> mem[params[2]] = result }
    ),
    HALT(99,
      { _, _ -> intArrayOf() },
      { _, _ -> throw HaltException() },
      { _, _, _ -> Unit }
    )
  }

  private var instructionPointer = 0

  private fun instruction(opCode: Int) = Instruction.values().find { it.opCode == opCode } ?: error("Invalid opcode $opCode")

  fun tick(): Boolean {
    val instruction = instruction(memory[instructionPointer])
    if(instruction == Instruction.HALT) return false

    val params = instruction.params(memory, instructionPointer)
    val result = instruction.fn(memory, params)
    instruction.output(memory, params, result)
    instructionPointer += params.size + 1
    return true
  }
}

fun runProgram(memory: IntArray, noun: Int, verb: Int): Int {
  memory[1] = noun
  memory[2] = verb
  val c = IntCodeComputer(memory)
  while(c.tick()) { /* do nothing */}
  return c.memory[0]
}
