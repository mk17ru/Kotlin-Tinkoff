package ru.tinkoff.kotlin.student.table

import org.jetbrains.exposed.dao.id.IntIdTable

object Groups: IntIdTable() {
    val number = integer("number")
}