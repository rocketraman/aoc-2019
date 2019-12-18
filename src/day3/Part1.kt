package day3

import input.input
import kotlin.math.abs

data class Point(val x: Int, val y: Int) {
  companion object {
    val ORIGIN = Point(0, 0)
  }

  fun nextPoint(direction: Char) = when(direction) {
    'U' -> copy(y = y + 1)
    'R' -> copy(x = x + 1)
    'D' -> copy(y = y - 1)
    'L' -> copy(x = x - 1)
    else -> error("Unknown direction $direction")
  }

  fun distanceTo(other: Point): Int = abs(x - other.x) + abs(y - other.y)
}

data class WireSegment(val points: List<Point>) {
  companion object {
    operator fun invoke(start: Point, direction: Char, steps: Int) = WireSegment(sequence {
      var current = start
      yield(current)
      repeat(steps) {
        current = current.nextPoint(direction)
        yield(current)
      }
    }.toList())
  }
}

data class Wire(val points: List<Point>)

fun main() {
  val wires = input(3/*, "test.txt"*/)
    .map { it.split(',') }
    .map { it.fold(emptyList<WireSegment>()) { wireSegments, pathElem ->
      val start = if(wireSegments.isEmpty()) Point.ORIGIN else wireSegments.last().points.last()
      wireSegments + WireSegment(start.nextPoint(pathElem.first()), pathElem.first(), pathElem.drop(1).toInt() - 1)
    }}
    .map { it.fold(Wire(emptyList())) { wire, segment -> wire.copy(points = wire.points + segment.points) } }

  // always 2 wires
  val wire1 = wires[0]
  val wire2 = wires[1]

  val intersections = wire1.points.intersect(wire2.points)

  // part 1
  val distance = intersections.map { it.distanceTo(Point.ORIGIN) }.min()
  println("distance = $distance")

  // part 2
  val steps = intersections.map { wire1.points.indexOf(it) + 1 + wire2.points.indexOf(it) + 1 }.min()
  println("steps = $steps")
}
