package input

import java.nio.file.*

fun input(day: Int, file: String = "input.txt") = Files.readAllLines(Paths.get("src/day%s/%s".format(day, file)))
