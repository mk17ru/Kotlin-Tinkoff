package lesson9.lesson.repository

import lesson9.lesson.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByLogin(login : String) : User?
    fun save(user: User) : User
}