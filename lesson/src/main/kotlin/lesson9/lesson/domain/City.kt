package lesson9.lesson.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class City(@Id @GeneratedValue var id: Long, var name: String) {
    constructor() : this(0, "") {

    }
}