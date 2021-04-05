import java.util.*
import java.util.concurrent.*
import kotlin.collections.ArrayList
import kotlin.random.Random
import kotlin.time.seconds

fun main() {
    printTaskName(1, "Create thread with different ways:")
    val t = MyThread().apply { start() }
    val r = Thread(MyRunnable()).start()
    val dsl = Thread {
        println("I'm dsl thread. My priority is ${Thread.currentThread().priority}")
    }.start()
    val someThread = Thread {
        println("I'm daemon!")
        Thread.sleep(100000)
        println("Program shouldn't wait this message, because I'm daemon.")
    }
    someThread.isDaemon = true
    someThread.start();
    priorityThreads()
    Thread.sleep(1000);
    printTaskName(2, "One thread write, other wait.")
    val consCount = arrayOf(0, 0, 0, 0)
    val dataBase = DataBase()
    val arrayThreads = ArrayList<Thread>()
    for (i in 0..2) {
        arrayThreads.add(Thread {
            val consumer = Consumer(dataBase)
            while (!dataBase.endingReading) {
                val data = consumer.get()
                if (data != null) {
                    //println("$i consumer get data: $data")
                    consCount[i]++;
                }
            }
        })
        arrayThreads.last().start()
    }
    val producer = Thread{
        val producer = Producer(dataBase)
        for (i in 1..10000) {
            producer.set(Random.nextInt().toString())
        }
        dataBase.endingReading = true
    }
    producer.start()
    producer.join()
    for (i in 0..2) {
        arrayThreads[i].join()
    }

    println("Results calls for threads: ${consCount[0]} ${consCount[1]} ${consCount[2]}")
    printTaskName(3, "Program with po:")
    increments()

}

fun increments() {
    val futures = mutableListOf<Future<Pair<Long, Int>>>()
    for (i in 1..3) {
        val executors = Executors.newFixedThreadPool(i * 10)
        val start = System.nanoTime()
        executors.submit(Callable {
            var counter = 0
            repeat(1_000_000) {
                counter++
            }
            if (counter == 1_000_000) {
                println("YES $i")
            }
            Pair(System.nanoTime() - start, i)
        }).also{futures.add(it)}
        executors.shutdown()
    }
    futures.sortWith(compareBy({it.get().first}, {it.get().second}))
    futures.forEach{
        println("${it.get().first} ${it.get().second}")
    }
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