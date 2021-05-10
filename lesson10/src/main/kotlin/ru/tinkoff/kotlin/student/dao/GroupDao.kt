package ru.tinkoff.kotlin.student.dao

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.tinkoff.kotlin.student.model.Group
import ru.tinkoff.kotlin.student.model.Student
import ru.tinkoff.kotlin.student.table.Groups
import ru.tinkoff.kotlin.student.table.Students
import java.util.*
import java.util.stream.Collectors
import org.jetbrains.exposed.sql.Database


class GroupDao(private val database : Database, private val studentDao: StudentDao) {

    fun findAll() = transaction(database) {
        Groups.selectAll().map(::extractGroup)
    }

    fun addToGroup(number : Int, students : List<Student>) : Group = transaction(database) {
        val group = Groups.select{Groups.number eq number}
        val id =
            if (!group.any())
                Groups.insertAndGetId { it[Groups.number] = number}
            else
                {group.single()[Groups.id]}
        addStudents(number, students)
        Group(id.value, number, students = students);
    }

    fun addStudents(number : Int, students : List<Student>) {
        for (st in students) {
            val curStudent = studentDao.find(st.name, st.surname)
            if (curStudent == null) {
                studentDao.create(st.name, st.surname, st.group)
            } else {
                studentDao.changeGroup(curStudent.id, number)
            }
        }
    }

    private fun extractGroup(row : ResultRow) : Group = Group(
        row[Groups.id].value,
        row[Groups.number],
        studentDao.findStudentsByGroup(row[Groups.number])
    )

    fun delete(number : Int) = transaction(database) {
        Groups.deleteWhere { Groups.number eq number}
    }



}