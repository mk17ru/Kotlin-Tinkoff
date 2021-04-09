import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce
import kotlin.math.abs
import kotlin.random.Random


val fileName = "/Kotlin-Tinkoff/lesson8/src/main/resources/test.db"
val DB_URL = "jdbc:sqlite:/Kotlin-Tinkoff/lesson8/src/main/resources/test.db"
@ExperimentalCoroutinesApi
suspend fun main() = runBlocking {
//    printTaskName(1, "Two different services: get a user and generate password and cypher it")
//    try {
//        val isExists = File(fileName).exists()
//        val conn = DriverManager.getConnection(DB_URL).use {
//            val dataBase = ServiceOne(it)
//            val users = UsersDAO().getUsers()
//            val service = ServiceOptional()
//            val init = TableInitializator(dataBase)
//            if (!isExists) {
//                init.createTables();
//            }
//            val list = synchronizedList(ArrayList<UserWithPassword>())
//            for (i in 1..5) {
//                for (j in users.indices) {
//                    val job = GlobalScope.launch {
//                        list.add(createUserWithPassword(dataBase, users[j], service))
//                    }
//                    job.join()
//                }
//            }
//            list.forEach{mem -> println(mem)}
//        }
//    } catch (exception : SQLException) {
//        println(exception.message)
//    }

    printTaskName(2, "Subscribe to the some event")

    val eventGenerator = eventGenerator()
    val eventService = EventService()
    val sg = subscribeGenerator(eventService)

    val job = GlobalScope.launch {
        repeat(20) {
            eventService.addEvent(eventGenerator.receive())
            var request = sg.receive()
            while(request.second == null) {
                delay(1000);
                request = sg.receive()
            }
            eventService.subscribe(request as Pair<User, Event>)
        }
    }
    job.join()
    coroutineContext.cancelChildren()
    for (i in eventService.getUsersForEvent()) {
        println("${i.key}")
        println("${i.value}")
        println()
    }
}


@ExperimentalCoroutinesApi
fun CoroutineScope.eventGenerator() = produce {
        var eventNum = 0
        var maxNumberOfPeople = 0
        while(true) {
            maxNumberOfPeople = abs(Random.nextInt()) % 10000
            eventNum++
            send(Event("$eventNum event", maxNumberOfPeople))
            delay(100);
        }

}

@ExperimentalCoroutinesApi
fun CoroutineScope.subscribeGenerator(eventService: EventService) = produce {
        var num = 0
        while (true) {
            num++
            val events = eventService.getEvents()
            send(Pair(User(num, "Mikhail", "Kozlov"),
                if (events.size != 0) events[abs(Random.nextInt()) % events.size] else null))
            if (abs(Random.nextInt()) % 2 == 0) {
                send(
                    Pair(
                        User(num, "Arkadiy", "Markov"),
                        if (events.size != 0) events[abs(Random.nextInt()) % events.size] else null
                    )
                )
                send(
                    Pair(
                        User(num, "Arkadiy", "Markov"),
                        if (events.size != 0) events[abs(Random.nextInt()) % events.size] else null
                    )
                )
            }
            if (abs(Random.nextInt()) % 3 == 0) {
                send(Pair(User(num, "Aleksey", "Popov"),
                    if (events.size != 0) events[abs(Random.nextInt()) % events.size] else null))
            }
            delay(30);
        }
}

suspend fun createUserWithPassword(dataBase : ServiceOne, user : User, service : ServiceOptional) : UserWithPassword {
    val deferred1 = GlobalScope.async { dataBase.insertUser(user); }
    val deferred2 = GlobalScope.async { service.generatePassword() }
    return UserWithPassword(deferred1.await(), deferred2.await())
}

fun printTaskName(num : Int, text : String)  {
    println("------------------------------------")
    println("$num task:")
    println(text)
    println("Solution:")
}
