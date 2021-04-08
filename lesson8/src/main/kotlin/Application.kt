import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.security.Provider
import java.sql.DriverManager
import java.sql.SQLException


val fileName = "/Kotlin-Tinkoff/lesson5/src/main/resources/test.db"
val DB_URL = "jdbc:sqlite:/Kotlin-Tinkoff/lesson5/src/main/resources/test.db"
fun main() {
    printTaskName(1, "Two different services: get a user and generate password and cypher it")
    try {
        val conn = DriverManager.getConnection(DB_URL).use {
            val isExists = File(fileName).exists()
            val dataBase = Service(it)
            val init = TableInitializator(dataBase)
            if (!isExists) {
                init.createTables();
            }

            for (i in 1..10000) {

                val serviceOne = ServiceOne()
                val job = GlobalScope.async {
                    C
                }
            }

            val job2 = GlobalScope.async {
                val s = ServiceOptional()
                s.generatePassword()
            }
        }
    } catch (exception : SQLException) {
        println(exception.message)
    }

}


fun printTaskName(num : Int, text : String)  {
    println("------------------------------------")
    println("$num task:")
    println(text)
    println("Solution:")
}
