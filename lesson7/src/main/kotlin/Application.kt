
fun main() {
    printTaskName(1, "Create thread with different ways:")
    val t = MyThread().apply { start() }
    val r = Thread(MyRunnable()).start()
    val dsl = Thread {
        println("I'm dsl thread. My priority is ${Thread.currentThread().priority}")
    }.start()
    val someThread = Thread {
        println("I'm daemon!")
        Thread.sleep(4000)
        println("I can't print this, because I'm daemon.")
    }
    someThread.isDaemon = true
    someThread.start();
    priorityThreads()
    printTaskName(2, "One thread write, other wait.")

}


fun priorityThreads() {
    val minPriorityThread = Thread {
        Thread.sleep(200)
        println("I'm min priority Thread!")
    }
    minPriorityThread.priority = Thread.MIN_PRIORITY
    minPriorityThread.start()
    val normPriorityThread = Thread {
        Thread.sleep(200)
        println("I'm norm priority Thread!")
    }
    normPriorityThread.priority = Thread.NORM_PRIORITY
    normPriorityThread.start()
    val maxPriorityThread = Thread {
        Thread.sleep(200)
        println("I'm max priority Thread!")
    }
    maxPriorityThread.priority = Thread.MAX_PRIORITY
    maxPriorityThread.start()
    println(normPriorityThread.isAlive)
}

fun printTaskName(num : Int, text : String)  {
    println("------------------------------------")
    println("$num task:")
    println(text)
    println("Solution:")
}

class MyThread : Thread() {
    override fun run() {
        println("I'm Thread.")
        println("Start sleep...")
        sleep(1000);
        println("End sleep...")
        println("I'm Thread. My id is ${currentThread().id}")
    }
}

class MyRunnable : Runnable {
    override fun run() {
        println("I'm Runnable. My name is ${Thread.currentThread().name}")

    }

}