package ru.tinkoff.kotlin.Group.service

import ru.tinkoff.kotlin.student.dao.GroupDao
import ru.tinkoff.kotlin.student.model.Group
import ru.tinkoff.kotlin.student.model.Student

class GroupService(private val dao: GroupDao) {
    fun findAll() : List<Group> = dao.findAll()
    fun addToGroup(number: Int, students: List<Student>) : Group = dao.addToGroup(number, students)
    fun delete(number : Int) = dao.delete(number)
//    fun update(id : Int, Group : Group) = dao.update(id, Group)
}
