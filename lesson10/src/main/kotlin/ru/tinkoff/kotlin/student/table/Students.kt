package ru.tinkoff.kotlin.student.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Students : IntIdTable() {
    val name = text("name")
    val surname = text("surname")
    val group = integer("group").references( Groups.id, onDelete = ReferenceOption.CASCADE)
}