package day6

fun orbits(mapData: List<List<String>>): TreeNode<String> {
  val nodes = mapData.flatten().toSet().map { it to TreeNode(it) }.toMap()

  mapData.map { it.let { it[0] to it[1] } }.forEach { orbit ->
    nodes.getValue(orbit.first).addChild(nodes.getValue(orbit.second))
  }

  return nodes.getValue("COM")
}
