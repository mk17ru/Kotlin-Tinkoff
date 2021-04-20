package lesson9.lesson.service

import lesson9.lesson.domain.User
import lesson9.lesson.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {

    fun addUser(user : User) {
        userRepository.save(user)
    }

    fun removeUser(id : Long) {
        userRepository.deleteById(id)
    }

    fun findUserById(id: Long) : Optional<User> {
        return userRepository.findById(id)
    }

    fun updateUserLoginById(id : Long, login : String) : Boolean {
        val user = userRepository.findById(id).orElse(null)
        return if (user == null) {
            false
        } else {
            user.login = login
            userRepository.save(user)
            true
        }
    }

    fun findAll(): MutableList<User> {
        return userRepository.findAll()
    }
}