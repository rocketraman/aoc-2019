package day4

fun main() {
  val input = 271973..785961

  // part 2
  println(input.count { i -> meetsCriteria(i) { it == 2 } })
}
