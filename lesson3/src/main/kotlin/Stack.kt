import java.util.*

class Stack<T>(elements: List<T>) {
    private val elements : LinkedList<T> = LinkedList<T>(elements)

    fun push(element: T) {
        elements.addLast(element)
    }

    fun pop() : T {
        return elements.pollLast()
    }

    fun size(): Int {
        return elements.size;
    }

    fun <T> stackOf(vararg els: T): Stack<T> {
        return if (els.isNotEmpty()) {
            Stack(els.asList())
        } else {
            Stack(emptyList())
        }
    }

    fun iterator(): Iterator<T> = elements.iterator()

    fun empty() : Boolean {
        return elements.isEmpty()
    }
}