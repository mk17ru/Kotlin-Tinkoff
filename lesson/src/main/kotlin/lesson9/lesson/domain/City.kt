package lesson9.lesson.domain

import javax.persistence.*

@Entity
class City(@Id @GeneratedValue var id: Long=0, var name: String)