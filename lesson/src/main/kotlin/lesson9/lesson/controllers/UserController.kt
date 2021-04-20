package lesson9.lesson.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lesson9.lesson.domain.User
import lesson9.lesson.repository.CityRepository
import lesson9.lesson.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus.*
import org.springframework.web.server.ResponseStatusException


@Tag(name = "user", description = "The User API")
@Controller
@RequestMapping("/api/user")
class UserController(private val userRepository: UserService, private val cityRepository: CityRepository) {

    @Operation(summary = "Gets user", tags = ["user"])
    @GetMapping("/get/{id}")
    @ResponseBody
    fun findUser(@PathVariable id: Long) : User = userRepository.findUserById(id).orElse(null)
                                ?: throw ResponseStatusException(NOT_FOUND, "User don't exist")


    @Operation(summary = "Gets all users", tags = ["user"])
    @GetMapping("/get/all")
    @ResponseBody
    fun findUsers() : MutableList<User> = userRepository.findAll()


    @Operation(summary = "Add user", tags = ["user"])
    @PostMapping("/save/{login}/{firstname}/{lastname}/{city}")
    @ResponseBody
    fun addUser(@PathVariable login: String, @PathVariable firstname: String, @PathVariable lastname: String,
                                                @PathVariable city: String): String {
        val c = cityRepository.findCityByName(city) ?: throw ResponseStatusException(NOT_FOUND, "City don't exist")
        userRepository.addUser(User(0, login, firstname, lastname, c.id))
        return "UserSaved"
    }

    @Operation(summary = "Delete user", tags = ["user"])
    @DeleteMapping("/remove/{id}")
    @ResponseBody
    fun deleteUser(@PathVariable id: Long) = userRepository.removeUser(id)


    @Operation(summary = "Update user", tags = ["user"])
    @PutMapping("/update/{id}/{login}")
    @ResponseBody
    fun updateUser(@PathVariable id: Long, @PathVariable login : String): String {

        val f = userRepository.updateUserLoginById(id, login)
        if (f) {
            return "User will be update!"
        } else {
            throw ResponseStatusException(NOT_FOUND, "User don't exist")
        }
    }


}