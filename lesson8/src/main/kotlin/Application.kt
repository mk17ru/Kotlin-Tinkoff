import kotlinx.coroutines.*
import java.io.File
import java.security.Provider
import java.sql.DriverManager
import java.sql.SQLException
import java.util.Collections.synchronizedList


val fileName = "/Kotlin-Tinkoff/lesson8/src/main/resources/test.db"
val DB_URL = "jdbc:sqlite:/Kotlin-Tinkoff/lesson8/src/main/resources/test.db"
suspend fun main() {
    printTaskName(1, "Two different services: get a user and generate password and cypher it")
    try {
        val isExists = File(fileName).exists()
        val conn = DriverManager.getConnection(DB_URL).use {
            val dataBase = ServiceOne(it)
            val users = UsersDAO().getUsers()
            val service = ServiceOptional()
            val init = TableInitializator(dataBase)
            if (!isExists) {
                init.createTables();
            }
            val list = synchronizedList(ArrayList<UserWithPassword>())
            for (i in 1..200) {
                for (j in users.indices) {
                    val job = GlobalScope.launch {
                        list.add(createUserWithPassword(dataBase, users[j], service))
                    }
                    job.join()
                }
            }
            list.forEach{mem -> println(mem)}
        }
    } catch (exception : SQLException) {
        println(exception.message)
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
