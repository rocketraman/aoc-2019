package day5

import kotlin.math.pow

class IntCodeComputer(val memory: IntArray, val ioInput: () -> Int, val ioOutput: (Int) -> Unit) {
  enum class Instruction(
    val opCode: Int,
    val paramCount: Int,
    val fn: (c: IntCodeComputer, params: IntArray, inputParams: IntArray) -> Boolean) {

    ADD(1,
      3,
      { c, params, inputParams -> c.memory[params[2]] = inputParams[0] + inputParams[1]; false }
    ),
    MULTIPLY(2,
      3,
      { c, params, inputParams -> c.memory[params[2]] = inputParams[0] * inputParams[1]; false }
    ),
    INPUT(3,
      1,
      { c, params, _ -> c.memory[params[0]] = c.ioInput(); false }
    ),
    OUTPUT(4,
      1,
      { c, _, inputParams -> c.ioOutput(inputParams[0]); false }
    ),
    JUMP_IF_TRUE(5,
      2,
      { c, params, inputParams -> if(inputParams[0] != 0) { c.instructionPointer = inputParams[1]; true } else false }
    ),
    JUMP_IF_FALSE(6,
      2,
      { c, params, inputParams -> if(inputParams[0] == 0) { c.instructionPointer = inputParams[1]; true } else false }
    ),
    LESS_THAN(7,
      3,
      { c, params, inputParams -> c.memory[params[2]] = if(inputParams[0] < inputParams[1]) 1 else 0; false }
    ),
    EQUALS_THAN(8,
      3,
      { c, params, inputParams -> c.memory[params[2]] = if(inputParams[0] == inputParams[1]) 1 else 0; false }
    ),
    HALT(99,
      0,
      { _, _, _ -> true }
    )
  }

  private var instructionPointer = 0

  private fun instruction(opCode: Int) = Instruction.values().find { it.opCode == opCode }
    ?: error("Invalid opcode $opCode at instruction pointer $instructionPointer, instruction=${memory[instructionPointer]}")

  fun tick(): Boolean {
    val opCode = memory[instructionPointer] % 100
    val instruction = instruction(opCode)
    if (instruction == Instruction.HALT) return false

    val params = IntArray(instruction.paramCount) {
      memory[instructionPointer + it + 1]
    }
    val paramModes = IntArray(params.size) {
      memory[instructionPointer] / 100 / (10.toDouble().pow(it.toDouble())).toInt() % 10
    }
    val inputParams = paramModes.zip(params).map {
      when (it.first) {
        0 -> memory[it.second]
        1 -> it.second
        else -> error("Unknown parameter mode ${it.first} at instruction pointer $instructionPointer, instruction=${memory[instructionPointer]}, opCode=$opCode, params=$params")
      }
    }.toIntArray()

    //println("Instruction $instruction (${instruction.opCode}) with params=[${params.joinToString()}], modes=[${paramModes.joinToString()}], inputParams=[${inputParams.joinToString()}]")

    val jump = instruction.fn(this, params, inputParams)
    if(!jump) instructionPointer += params.size + 1

    return true
  }
}

fun runProgram(memory: IntArray, ioInput: () -> Int, ioOutput: (Int) -> Unit) {
  val c = IntCodeComputer(memory, ioInput, ioOutput)
  while(c.tick()) { /* do nothing */}
}
