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
    println("---------------------------------")
    val que = Queue.queueOf(1, 2, 3, 4, 5);
    while(!que.empty()) {
        println(que.dequeue())
    }
    println("---------------------------------")
    val sta = Stack.stackOf(1, 2, 3, 4, 5);
    while(!sta.empty()) {
        println(sta.pop())
    }

}