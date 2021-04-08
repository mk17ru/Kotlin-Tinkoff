import java.sql.Connection
import java.sql.Statement
import java.util.ArrayList

class ServiceOne(conn: Connection) {
    private val client : ClientOne = ClientOne(conn)

    fun addUsers() {
        client.addUsers()
    }

    fun addPosts() {
        client.addPosts()
    }

    fun addRoles() {
        client.addRoles()
    }

    fun addUserRoles() {
        client.addUserRoles()
    }

    fun createTable(tableName: String, vararg fields: String) {
        client.createTable(tableName, fields)
    }

    fun findUsersByName(name : String) : List<User> {
        return client.findUsersByName(name, "id")
    }


    fun findElementsGreaterThanId(tableName: String, id : Int, field : String) : List<String> {
        return client.findElementsGreaterThanId(tableName, id, field)
    }


    fun groupUsersByRoles(): ArrayList<UserWithRoles> {
        return client.groupUsersByRoles();
    }

    fun findUsersSortBy(name: String, sortValue : String): List<User> {
        return client.findUsersByName(name, sortValue)
    }

    suspend fun insertUser(user : User) : User {
        client.insertUser(user)
        return user
    }
}