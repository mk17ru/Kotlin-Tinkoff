import java.sql.Connection
import java.sql.Statement
import java.util.ArrayList

class Service(stm: Statement, conn: Connection) {
    private val client : Client = Client(stm, conn)

    fun addUsers() {
        val userData = UsersDAO()
        for (i in userData.getUsers()) {
            client.insertValue(
                "'users'", arrayOf("'name'", "'surname'"),
                "'${i.name}'", "'${i.surname}'"
            )
        }
    }

    fun addPosts() {
        val postData = PostsDAO()
        for (i in postData.getPosts()) {
            client.insertValue(
                "'posts'", arrayOf("'user_id'", "'text'"),
                "'${i.userId}'", "'${i.text}'"
            )
        }
    }

    fun addRoles() {
        for (i in Role.values()) {
            client.insertValue("'roles'", arrayOf("'role'"), "'${i}'")
        }
    }

    fun addUserRoles() {
        val userRolesData = UserRolesDAO()
        for (userRole in userRolesData.getUserRoles()) {
            val list = findRolesIdByName(userRole.user_role)
            println("ROLE ID: " + list[0])
            client.insertValue(
                "'user_roles'", arrayOf("'user_id'", "'role_id'"),
                "'${userRole.userId}'", "'${list[0]}'"
            )
        }
    }

    fun createTable(tableName: String, vararg fields: String) {
        client.createTable(tableName, fields)
    }

    fun findUsersByName(name : String) : List<User> {
        return client.findUsersByName(name, "id")
    }

    fun findRolesIdByName(role: Role) : List<String> {
        return client.selectValue("roles", arrayOf("id"), null," WHERE role = '${role}'")
    }

    fun findElementsGreaterThanId(tableName: String, id : Int, field : String) : List<String> {
        return client.findElementsGreaterThanId(tableName, id, field)
    }

    fun getInnerUsersAndPosts(firstTableName: String, secondTableName: String,
                                    firstFields : Array<String>, secondFields : Array<String>,
                                    firstCommonField : String, secondCommonField : String): List<UserAndPosts> {
        return client.getInnerUsersAndPosts(firstTableName, secondTableName, firstFields, secondFields,
                                                            firstCommonField, secondCommonField)
    }

    fun getInnerLeftUsersAndPosts(firstTableName: String, secondTableName: String,
                              firstFields : Array<String>, secondFields : Array<String>,
                              firstCommonField : String, secondCommonField : String): List<UserAndPosts> {
        return client.getInnerLeftUsersAndPosts(firstTableName, secondTableName, firstFields, secondFields,
            firstCommonField, secondCommonField)
    }

    fun groupUsersByRoles(): ArrayList<UserWithRoles> {
        return client.groupUsersByRoles();
    }

    fun findUsersSortBy(name: String, sortValue : String): List<User> {
        return client.findUsersByName(name, sortValue)
    }
}