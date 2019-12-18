package day4

fun meetsCriteria(i: Int, adjacentCriteria: (Int) -> Boolean): Boolean {
  val password = i.toString()
  if(password.length != 6) return false
  if(password.windowed(2).any { it[1] < it[0] /* can compare char value, no need to convert */ }) return false
  if(password
    .groupBy { it } // this works because of the previous rule, we know the digits with the same value will always be adjacent
    .mapValues { it.value.size }
    .values
    .none { adjacentCriteria(it) }) return false
  return true
}

fun main() {
  val input = 271973..785961

  // part 1
  println(input.count { i -> meetsCriteria(i) { it >= 2 } })
}
