import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import java.sql.Connection
import java.sql.ResultSet
import java.util.*
import kotlin.collections.ArrayList

class Client(private val conn : Connection) {

    /**
     * Insert values in table
     */
    fun <T> insertValue(tableName: String, columnNames: Array<String>, vararg fields : T) {
        val sb = StringBuilder();
        sb.append("INSERT INTO $tableName ")
        addArguments(sb, columnNames, true)
        sb.append(" VALUES(")
        // CREATE (?, ?, ?, ...) for VALUES(?, ?, ...)
        sb.append("?, ".repeat(fields.size).substring(0, 3 * fields.size - 2))
        sb.append(" ")
        val stm = conn.prepareStatement(sb.toString())
        // REPLACE ? for the inserted fields
        for (i in fields.indices) {
            if (fields[i] is Int) {
                stm.setInt(i + 1, fields[i].toString().toInt())
            } else {
                stm.setString(i + 1, fields[i].toString())
            }
        }
        stm.execute()
    }

    fun addUsers() {
        val userData = UsersDAO()
        for (i in userData.getUsers()) {
            insertValue(
                "users", arrayOf("name", "surname"), i.name, i.surname
            )
        }
    }

    fun addPosts() {
        val postData = PostsDAO()
        for (i in postData.getPosts()) {
            insertValue(
                "posts", arrayOf("user_id", "text"), "${i.userId}", i.text
            )
        }
    }

    fun addRoles() {
        for (i in Role.values()) {
            insertValue("roles", arrayOf("role"), "$i")
        }
    }

    fun addUserRoles() {
        val userRolesData = UserRolesDAO()
        for (userRole in userRolesData.getUserRoles()) {
            val id = findRolesIdByName(userRole.user_role)
            insertValue(
                "user_roles", arrayOf("user_id", "role_id"),
                userRole.userId, id
            )
        }
    }

    fun findRolesIdByName(userRole: Role): Int {
        var id: Int
        selectValue("roles", arrayOf("id"),"role", userRole.toString(), "role", false)
            .use {
                resultSet ->
                id = if (resultSet.next()) 0 else resultSet.getInt("id")
            }
        return id
    }

    fun addArguments(sb : StringBuilder, fields : Array<out String>, needBrackets : Boolean) {
        if (needBrackets) {
            sb.append("(")
        }
        sb.append(fields.joinToString(", "))
        if (needBrackets) {
            sb.append(")")
        }
    }

    fun createTable(tableName: String, fields: Array<out String>) {
        val stm = conn.createStatement()
        val sb = StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS $tableName ");
        addArguments(sb, fields, true);
        println(sb.toString())
        stm.executeUpdate(sb.toString())
    }
    /**
     * Select values from table
     * @param tableName table
     * @param columnNames columns which select
     * @param field criterion for select
     * @param sortValue value which is the key for sorting
     * @param DESCOrder is DESC order or not default value is false
     */
    fun selectValue(tableName: String, columnNames: Array<String>, field: String, value : String, sortValue: String,
                                                    DESCOrder : Boolean=false) : ResultSet {
        val sb = StringBuilder();
        sb.append("SELECT ");
        addArguments(sb, columnNames, false)
        sb.append(" FROM $tableName")
        sb.append(" WHERE $field = ? ")
        if (DESCOrder) {
            sb.append("ORDER BY $sortValue DESC")
        } else {
            sb.append("ORDER BY $sortValue ASC")
        }
        val stm = conn.prepareStatement(sb.toString());
        stm.setString(1, value);
        return stm.executeQuery()
    }

    fun findUsersByName(name : String, sortValue: String, DESCOrder: Boolean=false) : List<User> {
        val rs = selectValue("users", arrayOf("id", "name", "surname"), "name", name, sortValue, DESCOrder)
        val userList = ArrayList<User>()
        while (rs.next()) {
            userList.add(User(
                id = rs.getInt("id"),
                name = rs.getString("name"),
                surname = rs.getString("surname")
            ))
        }
        return userList
    }

    fun findElementsGreaterThanId(tableName: String, id: Int, findingField: String): List<String> {
        val answer = ArrayList<String>();
        val sb = StringBuilder();
        sb.append("SELECT ")
        addArguments(sb, arrayOf(findingField), false)
        sb.append(" FROM $tableName WHERE id > ? ORDER BY id ASC")
        val rs = conn.prepareStatement(sb.toString()).apply {
            setString(1, id.toString());
        }.executeQuery()
        while (rs.next()) {
            answer.add(rs.getString(findingField))
        }
        return answer
    }


    /**
     * Create Inner Join Users and Posts
     * @param firstTableName first table name
     * @param secondTableName first table name
     * @param firstFileds fields from first table
     * @param secondFields fields from second table
     * @param firstCommonField filed from first table which use for join
     * @param secondCommonField filed from second table which use for join
     * @param type type of join
     * @return {@link List<UserAndPosts>}
     */
    fun innerJoin(firstTableName: String, secondTableName: String,
                         firstFields: Array<String>, secondFields: Array<String>,
                                        firstCommonField : String, secondCommonField : String,
                                                                    type : String) : ResultSet {
        val sb = StringBuilder();
        sb.append("SELECT ")
        val columnNames = firstFields + secondFields
        addArguments(sb, columnNames, false)
        sb.append(" FROM $firstTableName $type ")
        sb.append(" JOIN $secondTableName ON $firstTableName.$firstCommonField=$secondTableName.$secondCommonField")
        sb.append(" ORDER BY user_id ASC")
        return conn.prepareStatement(sb.toString()).executeQuery();
    }

    fun getInnerUsersAndPosts(firstTableName: String, secondTableName: String,
                    firstFields: Array<String>, secondFields: Array<String>,
                    firstCommonField : String, secondCommonField : String) : List<UserAndPosts> {
        return toUserAndPostsList(innerJoin(firstTableName, secondTableName, firstFields, secondFields,
            firstCommonField, secondCommonField, "INNER"), firstFields, secondFields);
    }


    /**
     * Create Left Inner Join Users and Posts
     * @param firstTableName first table name
     * @param secondTableName first table name
     * @param firstFileds fields from first table
     * @param secondFields fields from second table
     * @param firstCommonField filed from first table which use for join
     * @param secondCommonField filed from second table which use for join
     * @return {@link List<UserAndPosts>}
     */
    fun getInnerLeftUsersAndPosts(firstTableName: String, secondTableName: String, firstFields: Array<String>,
                                  secondFields: Array<String>, firstCommonField: String,
                                  secondCommonField: String): List<UserAndPosts> {
        return toUserAndPostsList(innerJoin(firstTableName, secondTableName, firstFields, secondFields,
            firstCommonField, secondCommonField, "LEFT"), firstFields, secondFields)
    }


    private fun toUserAndPostsList(rs: ResultSet, usersFields: Array<String>, postField: Array<String>)
                                                                                            : List<UserAndPosts> {
        val result = ArrayList<UserAndPosts>()
        val usersF = changeColumnsNames(usersFields)
        val postF = changeColumnsNames(postField)
        while (rs.next()) {
            result.add(UserAndPosts(
                userID = rs.getInt(usersF[0]),
                userName = rs.getString(usersF[1]),
                userSurname = rs.getString(usersF[2]) ,
                post = rs.getString(postF[0])
            ))
        }
        return result
    }

    /**
     * Group Users by their roles
     * @return ArrayList<ArrayList<UserWithRoles>> where Map role -> users
     */
    fun groupUsersByRoles(): ArrayList<UserWithRoles> {
        val result = ArrayList<UserWithRoles>()
        val sb = StringBuilder();
        val columnNames : Array<String> = arrayOf("roles.role", "GROUP_CONCAT(users.surname)")
        //SELECT BLOCK
            sb.append("SELECT ")
            addArguments(sb, columnNames, false)
            sb.append(" FROM roles ")
        //INNER
            sb.append(" INNER JOIN user_roles ON user_roles.role_id=roles.id")
            sb.append(" INNER JOIN users ON user_roles.user_id=users.id")
        // GROUP
            sb.append(" GROUP BY roles.role ORDER BY roles.id ASC")
        val rs = conn.prepareStatement(sb.toString()).executeQuery();
        while (rs.next()) {
            result.add(UserWithRoles(
                role = Role.valueOf(rs.getString("role")),
                users = rs.getString("GROUP_CONCAT(users.surname)"),
            ))
        }
        return result;
    }

    fun changeColumnsNames(columnNames: Array<String>): Array<String> {
        for (i in columnNames.indices) {
            if (columnNames[i].indexOf("GROUP_CONCAT") != -1) {
                continue;
            }
            columnNames[i] = columnNames[i].split(".").last()
        }
        return columnNames
    }

    
}
