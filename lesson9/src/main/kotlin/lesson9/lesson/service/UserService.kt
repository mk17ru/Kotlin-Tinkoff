package lesson9.lesson.service

import lesson9.lesson.domain.User
import lesson9.lesson.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {

    fun addUser(user : User) : User {
        return userRepository.save(user)
    }

    fun removeUser(id : Long) {
        userRepository.deleteById(id)
    }

    fun findUserById(id: Long) : Optional<User> {
        return userRepository.findById(id)
    }

    fun updateUser(id : Long, user : User) : User? {
        val u = userRepository.findById(id).orElse(null)
        return if (u != null) {
            u.firstname = user.firstname
            u.lastname = user.lastname
            u.login = user.login
            u.cityId = user.cityId
            userRepository.save(u)
        } else {
            null
        }
    }

    fun findAll(): List<User> {
        val d = userRepository.findAll()
        println(d)
        return d
    }
}