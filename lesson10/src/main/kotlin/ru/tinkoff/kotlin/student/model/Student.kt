package ru.tinkoff.kotlin.student.model

import kotlinx.serialization.Serializable

@Serializable
data class Student(val id: Int=0, val name: String, val surname: String, val group: Int=-1)