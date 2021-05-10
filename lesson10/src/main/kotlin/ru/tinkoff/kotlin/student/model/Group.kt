package ru.tinkoff.kotlin.student.model

import kotlinx.serialization.Serializable

@Serializable
class Group(val id : Int, val number : Int, val students : List<Student> = emptyList())