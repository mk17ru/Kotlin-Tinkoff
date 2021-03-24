import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import java.sql.Connection
import java.sql.Statement
import java.util.*
import kotlin.reflect.KFunction1

class Client(private val stm: Statement, private val conn : Connection) {

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
        val sb = StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS $tableName ");
        addArguments(sb, fields, true);
        println(sb.toString())
        stm.executeUpdate(sb.toString())
    }

    fun insertValue(tableName: String, columnNames: Array<String>, vararg fields : String) {
        val sb = StringBuilder();
        sb.append("INSERT INTO $tableName ");
        addArguments(sb, columnNames, true);
        sb.append(" VALUES ");
        addArguments(sb, fields, true);
        println(sb)
        stm.executeUpdate(sb.toString());
    }

    fun selectValue(tableName: String, columnNames: Array<String>,
                                changeNames: KFunction1<Array<String>, Array<String>>?,
                                                vararg fields: String) : List<String> {
        val answer = ArrayList<String>();
        val sb = StringBuilder();
        sb.append("SELECT ");
        addArguments(sb, columnNames, false)
        sb.append(" FROM $tableName")
        addArguments(sb, fields, false);
        println(sb)
        val ps = stm.executeQuery(sb.toString())
        var column = columnNames
        if (changeNames != null) {
            column = changeNames(columnNames)
        }
        while(ps.next()) {
            for (i in 1..ps.metaData.columnCount) {
                for (j in column) {
                    when (ps.metaData.getColumnName(i)) {
                        j -> {
                            answer.add(ps.getString(i))
                        }
                    }
                }
            }
        }
        return answer
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


    fun findUsersByName(name : String, sortValue: String) : List<User> {
        val list = selectValue("users", arrayOf("id", "name", "surname"), null,
                                                " WHERE name = '${name}' ORDER BY $sortValue DESC",
        )
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

    private fun toUserAndPosts(list: List<String>) : UserAndPosts {
        if (list.size != 4) {
            throw IllegalArgumentException("Can't create User expected 4 arguments, but found ${list.size}")
        }
        return UserAndPosts(Integer.parseInt(list[0]), list[1], list[2], list[3]);
    }

    fun findElementsGreaterThanId(tableName: String, id: Int, field: String): List<String> {
        return selectValue(tableName, arrayOf(field), null," WHERE id > ${id} " +
                "ORDER BY id ASC")
    }

    fun innerJoin(firstTableName: String, secondTableName: String,
                         firstFields: Array<String>, secondFields: Array<String>,
                                        firstCommonField : String, secondCommonField : String,
                                                                    type : String) : List<String> {
        return selectValue(firstTableName, firstFields + secondFields, this::changeColumnsNames,
        " $type JOIN $secondTableName ON $firstTableName.$firstCommonField=$secondTableName.$secondCommonField " +
                                                        "ORDER BY user_id ASC")
    }


    fun getInnerUsersAndPosts(firstTableName: String, secondTableName: String,
                    firstFields: Array<String>, secondFields: Array<String>,
                    firstCommonField : String, secondCommonField : String) : List<UserAndPosts> {
        return toUserAndPostsList(innerJoin(firstTableName, secondTableName, firstFields, secondFields,
            firstCommonField, secondCommonField, "INNER"));
    }

    fun getInnerLeftUsersAndPosts(firstTableName: String, secondTableName: String, firstFields: Array<String>,
                                  secondFields: Array<String>, firstCommonField: String,
                                  secondCommonField: String): List<UserAndPosts> {
        return toUserAndPostsList(innerJoin(firstTableName, secondTableName, firstFields, secondFields,
            firstCommonField, secondCommonField, "LEFT"))
    }

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

    fun groupUsersByRoles(): ArrayList<UserWithRoles> {
        val sb = StringBuilder()
        val list = selectValue("roles", arrayOf("roles.role", "GROUP_CONCAT(users.surname)"),
            this::changeColumnsNames,
        " INNER JOIN user_roles ON user_roles.role_id=roles.id INNER JOIN users ON user_roles.user_id=users.id" +
                " GROUP BY roles.role ORDER BY roles.id ASC")
        val result = ArrayList<UserWithRoles>()
        for (i in 0..list.size - 1) {
            if (i % 2 != 0) {
                continue
            }
            result.add(toUserRole(listOf(list[i], list[i + 1])))
        }
        return result;
    }

}
