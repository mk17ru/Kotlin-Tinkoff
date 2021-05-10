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
@RestController
@RequestMapping("/api/users")
class UserController(private val userRepository: UserService, private val cityRepository: CityRepository) {

    @Operation(summary = "Gets user", tags = ["user"])
    @GetMapping("/{id}")
    @ResponseBody
    @ResponseStatus(OK)
    fun findUser(@PathVariable id: Long) : User =
        userRepository.findUserById(id).orElse(null) ?: throw ResponseStatusException(NOT_FOUND, "User don't exist")


    @Operation(summary = "Gets all users", tags = ["user"])
    @GetMapping
    @ResponseStatus(OK)
    fun findUsers() : List<User> = userRepository.findAll()


    @Operation(summary = "Add user", tags = ["user"])
    @PostMapping
    @ResponseBody
    @ResponseStatus(CREATED)
    fun addUser(@RequestBody user : User): User {
        val c = cityRepository.findCityById(user.cityId)
                                            ?: throw ResponseStatusException(NOT_FOUND, "City don't exist")
        return userRepository.addUser(User(0, user.login, user.firstname, user.lastname, c.id))
    }

    @Operation(summary = "Delete user", tags = ["user"])
    @DeleteMapping("/{id}")
    @ResponseBody
    @ResponseStatus(OK)
    fun deleteUser(@PathVariable id: Long) = userRepository.removeUser(id)


    @Operation(summary = "Update user", tags = ["user"])
    @PutMapping("/{id}")
    @ResponseBody
    @ResponseStatus(OK)
    fun updateUser(@PathVariable id: Long, @RequestBody user : User): User {
        return userRepository.updateUser(id, user) ?: throw ResponseStatusException(NOT_FOUND, "User don't exist")
    }
}