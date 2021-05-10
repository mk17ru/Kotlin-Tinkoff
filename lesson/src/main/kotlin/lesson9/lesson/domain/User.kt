package lesson9.lesson.domain

import javax.persistence.*

@Entity
class User(
    @Id
    @GeneratedValue
    var id: Long=0,
    var login: String,
    var firstname: String,
    var lastname: String,
    var cityId: Long)