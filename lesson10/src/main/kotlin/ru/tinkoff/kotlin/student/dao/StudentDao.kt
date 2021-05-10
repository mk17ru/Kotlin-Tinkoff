package ru.tinkoff.kotlin.student.dao

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.tinkoff.kotlin.student.table.Students
import ru.tinkoff.kotlin.student.model.Student
import org.jetbrains.exposed.sql.Database
import java.util.stream.Collectors


class StudentDao(private val database : Database) {
    fun findAll() = transaction(database) {
        Students.selectAll().map(::extractStudent)
    }

    fun create(name : String, surname : String, group: Int) : Student = transaction(database) {
        val id = Students.insertAndGetId {
            it[Students.name] = name
            it[Students.surname] = surname
            it[Students.group] = group
        }
        Student(id.value, name, surname, group)
    }

    private fun extractStudent(row : ResultRow) : Student = Student(
        row[Students.id].value,
        row[Students.name],
        row[Students.surname],
        row[Students.group]
    )

    fun find(name : String, surname: String): Student? = transaction(database) {
        val student = Students.select { (Students.name eq name) and (Students.surname eq surname) }
        if (student.any()) {
            extractStudent(student.single())
        } else {
            null
        }
    }

    fun delete(id : Int) = transaction(database) {
        Students.deleteWhere { (Students.id eq id) }
    }

    fun update(id : Int, newStudent : Student) = transaction(database) {
        Students.update({ Students.id eq id}) {
            it[name] = newStudent.name
            it[surname] = newStudent.surname
            it[group] = newStudent.group
        }
    }

    fun findStudentsByGroup(number: Int): List<Student> = transaction(database) {
        Students.select { Students.group eq number}.toList().stream().map(::extractStudent).collect(Collectors.toList());
    }


    fun changeGroup(id : Int, newGroup : Int) = transaction(database) {
        Students.update({ Students.id eq id}) {
            it[group] = newGroup
        }
    }
}