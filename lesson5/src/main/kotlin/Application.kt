import java.io.File
import java.sql.DriverManager
import java.sql.SQLException

val fileName = "/Kotlin-Tinkoff/lesson5/src/main/resources/test.db"
val DB_URL = "jdbc:sqlite:/Kotlin-Tinkoff/lesson5/src/main/resources/test.db"
fun main() {
    try {
        val conn = DriverManager.getConnection(DB_URL)

        val isExists = File(fileName).exists()
        val dataBase = Service(conn)
        val init = TableInitializator(dataBase)
        if (!isExists) {
            init.createTables();
        }
        printTaskName(1, "Find users by name:");
        dataBase.findUsersByName("Mikhail").forEach { println(it) }
        printTaskName(2, "Find elements which id > 2")
        println("Users:")
        println(dataBase.findElementsGreaterThanId("users", 2, "surname"))
        println("Roles:")
        println(dataBase.findElementsGreaterThanId("roles", 2, "role"))
        printTaskName(3, "Join and inner join")
        println("Inner Join:")
        dataBase.getInnerUsersAndPosts(
            "users", "posts",
            arrayOf("users.id", "users.name", "users.surname"),
            arrayOf("posts.text"),
            "id", "user_id"
        ).forEach { println(it) }
        println();
        println("Inner Left Join:")
        dataBase.getInnerLeftUsersAndPosts(
            "users", "posts",
            arrayOf("users.id", "users.name", "users.surname"),
            arrayOf("posts.text"),
            "id", "user_id"
        ).forEach { println(it) }
        printTaskName(4, "Group by")
        dataBase.groupUsersByRoles().forEach{
            println("For role: ${it.role}, Users: ${it.users}")
        }
        printTaskName(5, "Sort User by surname")
        dataBase.findUsersSortBy("Mikhail", "surname").forEach { println(it) }
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

