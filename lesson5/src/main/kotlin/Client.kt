import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import java.sql.Connection
import java.sql.ResultSet
import java.util.*

class Client(private val conn : Connection) {

    fun addUsers() {
        val userData = UsersDAO()
        for (i in userData.getUsers()) {
            insertValue(
                "users", arrayOf("name", "surname"),
                "${i.name}", "${i.surname}"
            )
        }
    }

    fun addPosts() {
        val postData = PostsDAO()
        for (i in postData.getPosts()) {
            insertValue(
                "posts", arrayOf("user_id", "text"),
                "${i.userId}", i.text
            )
        }
    }

    fun addRoles() {
        for (i in Role.values()) {
            insertValue("roles", arrayOf("role"), "${i}")
        }
    }

    fun addUserRoles() {
        val userRolesData = UserRolesDAO()
        for (userRole in userRolesData.getUserRoles()) {
            val list = findRolesIdByName(userRole.user_role)
            println("ROLE ID: " + list[0])
            insertValue(
                "user_roles", arrayOf("user_id", "role_id"),
                userRole.userId, list[0]
            )
        }
    }

    fun findRolesIdByName(userRole: Role): List<String> {
        return selectValue("roles", arrayOf("id"),"role", userRole.toString(), "role", false)
    }

    fun addArguments(sb : StringBuilder, fields : Array<out String>, needBrackets : Boolean) {
        if (needBrackets) {
            sb.append("(");
        }
        for (i in fields.indices) {
            sb.append(fields[i])
            if (i + 1 != fields.size) {
                sb.append(", ")
            }
        };
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

    /**
     * Select values from table
     * @param tableName table
     * @param columnNames columns which select
     * @param field criterion for select
     * @param sortValue value which is the key for sorting
     * @param DESCOrder is DESC order or not default value is false
     */
    fun selectValue(tableName: String, columnNames: Array<String>, field: String, value : String, sortValue: String,
                                                    DESCOrder : Boolean=false) : List<String> {
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
        return chooseFromResultSet(stm.executeQuery(), columnNames);
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


    fun findUsersByName(name : String, sortValue: String, DESCOrder: Boolean=false) : List<User> {
        val list = selectValue("users",
                                    arrayOf("id", "name", "surname"),
                                                "name", name, sortValue, DESCOrder)
        val result = ArrayList<User>()
        if (list.size % 3 != 0) {
            throw IllegalArgumentException("Can't create Users expected % 3 == 0 number of arguments, " +
                    " (id, name, surname) but found ${list.size}")
        }
        for (i in 0..list.size - 2) {
            if (i % 3 != 0) {
                continue
            }
            result.add(toUser(listOf(list[i], list[i + 1], list[i + 2])))
        }
        return result
    }

    private fun toUser(list: List<String>) : User {
        if (list.size != 3) {
            throw IllegalArgumentException("Can't create User expected 3 arguments, but found ${list.size}")
        }
        return User(Integer.parseInt(list[0]), list[1], list[2])
    }

    private fun toUserRole(list: List<String>) : UserWithRoles {
        if (list.size != 2) {
            throw IllegalArgumentException("Can't create User expected 2 arguments, but found ${list.size}")
        }
        return UserWithRoles(Role.valueOf(list[0]), list[1])
    }

    fun findElementsGreaterThanId(tableName: String, id: Int, findingField: String): List<String> {
        val answer = ArrayList<String>();
        val sb = StringBuilder();
        sb.append("SELECT ")
        addArguments(sb, arrayOf(findingField), false)
        sb.append(" FROM $tableName WHERE id > ? ORDER BY id ASC")
        val stm = conn.prepareStatement(sb.toString());
        stm.setString(1, id.toString());
        chooseFromResultSet(stm.executeQuery(), arrayOf(findingField));
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
                                                                    type : String) : List<String> {
        val sb = StringBuilder();
        sb.append("SELECT ")
        var columnNames = firstFields + secondFields
        addArguments(sb, columnNames, false)
        sb.append(" FROM $firstTableName $type ")
        sb.append(" JOIN $secondTableName ON $firstTableName.$firstCommonField=$secondTableName.$secondCommonField")
        sb.append(" ORDER BY user_id ASC")
        columnNames = changeColumnsNames(columnNames)
        val stm = conn.prepareStatement(sb.toString());
        return chooseFromResultSet(stm.executeQuery(), columnNames);
    }

    /**
     * Choose columnNames from resultSet
     */
    fun chooseFromResultSet(rs : ResultSet, columnNames: Array<String>): ArrayList<String> {
        val answer = ArrayList<String>();
        while(rs.next()) {
            for (i in 1..rs.metaData.columnCount) {
                for (j in columnNames) {
                    when (rs.metaData.getColumnName(i)) {
                        j -> {
                            answer.add(rs.getString(i))
                        }
                    }
                }
            }
        }
        return answer
    }


    fun getInnerUsersAndPosts(firstTableName: String, secondTableName: String,
                    firstFields: Array<String>, secondFields: Array<String>,
                    firstCommonField : String, secondCommonField : String) : List<UserAndPosts> {
        return toUserAndPostsList(innerJoin(firstTableName, secondTableName, firstFields, secondFields,
            firstCommonField, secondCommonField, "INNER"));
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
            firstCommonField, secondCommonField, "LEFT"))
    }


    /**
     * Collect UserAnsPosts from List<String>
     */
    private fun toUserAndPosts(list: List<String>) : UserAndPosts {
        if (list.size != 4) {
            throw IllegalArgumentException("Can't create User expected 4 arguments, but found ${list.size}")
        }
        return UserAndPosts(Integer.parseInt(list[0]), list[1], list[2], list[3]);
    }

    /**
     * Collect UserAnsPosts to list by List<String>
     */
    private fun toUserAndPostsList(list: List<String>) : List<UserAndPosts> {
        val result = ArrayList<UserAndPosts>()
        for (i in 0..list.size - 3) {
            if (i % 4 != 0) {
                continue
            }
            result.add(toUserAndPosts(listOf(list[i], list[i + 1], list[i + 2], list[i + 3])))
        }
        return result;
    }

    /**
     * Group Users by their roles
     * @return ArrayList<ArrayList<UserWithRoles>> where Map role -> users
     */
    fun groupUsersByRoles(): ArrayList<UserWithRoles> {
        val result = ArrayList<UserWithRoles>()
        val sb = StringBuilder();
        var columnNames : Array<String> = arrayOf("roles.role", "GROUP_CONCAT(users.surname)")
        //SELECT BLOCK
            sb.append("SELECT ")
            addArguments(sb, columnNames, false)
            sb.append(" FROM roles ")
        //INNER
            sb.append(" INNER JOIN user_roles ON user_roles.role_id=roles.id")
            sb.append(" INNER JOIN users ON user_roles.user_id=users.id")
        // GROUP
            sb.append(" GROUP BY roles.role ORDER BY roles.id ASC")
        // STATEMENT
        val stm = conn.prepareStatement(sb.toString());
        // CHOOSE ANSWER FIELD FROM RESULT SET
        columnNames = changeColumnsNames(columnNames)
        val list = chooseFromResultSet(stm.executeQuery(), columnNames)
        for (i in list.indices) {
            if (i % 2 != 0) {
                continue
            }
            result.add(toUserRole(listOf(list[i], list[i + 1])))
        }
        return result;
    }

}
