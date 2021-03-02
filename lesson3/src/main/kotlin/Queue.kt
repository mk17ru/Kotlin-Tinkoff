import java.util.*

class Queue<T>(elements: List<T>) {
    private val elements : LinkedList<T> = LinkedList<T>(elements)

    fun enqueue(element: T) {
        elements.addLast(element)
    }

    fun dequeue() : T {
        return elements.pollFirst()
    }

    fun element() : T {
        return elements.last
    }

    fun size(): Int {
        return elements.size;
    }

    fun <T> queueOf(vararg els: T): Queue<T> {
        return if (els.isNotEmpty()) {
            Queue(els.asList())
        } else {
            Queue<T>(emptyList())
        }
    }

    fun empty() : Boolean {
        return elements.isEmpty()
    }

    operator fun iterator(): Iterator<T> = elements.iterator()

}