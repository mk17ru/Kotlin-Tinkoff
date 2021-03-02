fun main() {
    val queue = Queue(listOf("String", "Integer", "Boolean"));
    println(queue.dequeue())
    queue.enqueue("Char")
    while(!queue.empty()) {
        println(queue.dequeue())
    }
    println("---------------------------------")
    val stack = Stack(listOf("String", "Integer", "Boolean"));
    println(stack.pop())
    stack.push("Char")
    while(!stack.empty()) {
        println(stack.pop())
    }
}