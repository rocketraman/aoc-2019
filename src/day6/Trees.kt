package day6

import kotlin.collections.List

/**
 * A tree node. It is iterable, and iterates on nodes in pre-order traversal.
 */
class TreeNode<T>(var value: T, var label: String? = null): Iterable<TreeNode<T>> {
  /** A mutable backing field for children -- we don't expose this to the outside world. */
  private val _children = mutableListOf<TreeNode<T>>()

  var parent: TreeNode<T>? = null

  /** Read-only list of children. Use [addChild] to create a new child and [removeChild] to remove a child. */
  val children: List<TreeNode<T>> = _children

  fun isEmpty() = value == null && children.isEmpty()

  /**
   * Return tree as a flat list of values, in pre-order traversal, including the current value.
   */
  fun flat(): List<T> {
    return flatNodes().map { it.value }
  }

  /**
   * Return tree as a flat list of nodes, in pre-order traversal, including the current node.
   */
  fun flatNodes(): List<TreeNode<T>> {
    return listOf(this) + children.flatMap { it.flatNodes() }
  }

  /**
   * Return path from current node to root node as a flat List, not including the current value. The
   * parent value of this node is always first, the root value is always last.
   */
  fun path(): List<T> {
    return pathNodes().map { it.value }
  }

  /**
   * Return path from current node to root node as a flat List, not including the current node.  The
   * parent node of this node is always first, the root node is always last.
   */
  fun pathNodes(): List<TreeNode<T>> {
    val path = mutableListOf<TreeNode<T>>()
    var p: TreeNode<T>? = parent
    while(p != null) {
      path.add(p)
      p = p.parent
    }
    return path
  }

  fun siblingIndex(): Int {
    return parent?.children?.indexOf(this) ?: 0
  }

  /**
   * Add a child [TreeNode] to this [TreeNode], after any existing children.
   */
  fun addChild(node: TreeNode<T>) {
    _children.add(node)
    node.parent = this
  }

  /**
   * Remove a child [TreeNode] from this [TreeNode].
   * to remove it.
   * @return true if the element was successfully removed
   */
  fun removeChild(node: TreeNode<T>): Boolean {
    return _children.remove(node)
  }

  /**
   * Maps the values of a tree with the given transformer, and returns a new tree.
   */
  fun <R> mapValues(transformer: (T) -> R): TreeNode<R> {
    return TreeNode(transformer(value), label).apply {
      this@TreeNode.children.forEach { addChild(it.mapValues(transformer)) }
    }
  }

  /**
   * An iterator that returns nodes in pre-order traversal, including the current node.
   */
  override fun iterator(): Iterator<TreeNode<T>> = flatNodes().iterator()

  override fun equals(other: Any?): Boolean {
    return other != null && other is TreeNode<*> && value == other.value && label == other.label && children == other.children
  }

  override fun toString(): String {
    var s = value.toString()
    if (children.isNotEmpty()) {
      s += " {" + children.map { it.toString() } + " }"
    }
    return s
  }

  override fun hashCode(): Int {
    var result = value?.hashCode() ?: 0
    result = 31 * result + (label?.hashCode() ?: 0)
    result = 31 * result + (parent?.hashCode() ?: 0)
    result = 31 * result + children.hashCode()
    return result
  }
}

fun <T> TreeNode<T>.asString(level: Int = 0, builder: StringBuilder? = null, printer: (T) -> String = { it.toString() }): String {
  fun StringBuilder.appendTree() {
    repeat(level) {
      append("  ")
    }
    // appendln is system dependent and will append \r\n in Windows which in turns fails the
    // `Can print the tree` test in TreeNodeTest.kt
    append("${printer(value)}\n")

    for (child in children) {
      child.asString(level + 1, this, printer)
    }
  }

  return (builder ?: StringBuilder()).apply { appendTree() }.toString()
}

fun <T> TreeNode<T>.commonAncestor(other: TreeNode<T>): TreeNode<T>? {
  val path1 = this.pathNodes().reversed()
  val path2 = other.pathNodes().reversed()

  for (i in 0..(Integer.min(path1.size, path2.size))) {
    when {
      i >= path1.size -> return path1[i - 1]
      i >= path2.size -> return path2[i - 1]
      path1[i] != path2[i] -> return if(i > 0) path1[i - 1] else null
    }
  }
  return null
}
