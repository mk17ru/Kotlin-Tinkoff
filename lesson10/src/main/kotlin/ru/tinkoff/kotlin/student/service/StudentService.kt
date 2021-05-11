package ru.tinkoff.kotlin.student.service

import ru.tinkoff.kotlin.student.model.Student
import ru.tinkoff.kotlin.student.dao.StudentDao

class StudentService(private val dao: StudentDao) {
    fun findAll() : List<Student> = dao.findAll()
    fun find(name : String, surname: String) : Student? = dao.find(name, surname)
    fun find(id : Int) : Student = dao.find(id)
    fun create(name : String, surname : String, group: Int) : Student = dao.create(name, surname, group)
    fun delete(id : Int) = dao.delete(id)
    fun update(id : Int, student : Student) = dao.update(id, student)
    fun changeGroup(id : Int, group: Int) = dao.changeGroup(id, group)
    fun findStudentsByGroup(number: Int): List<Student> = dao.findStudentsByGroup(number)
}