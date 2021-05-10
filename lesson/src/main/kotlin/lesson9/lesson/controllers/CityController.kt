package lesson9.lesson.controllers

import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lesson9.lesson.domain.City
import lesson9.lesson.domain.User
import lesson9.lesson.repository.CityRepository
import lesson9.lesson.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus.*
import org.springframework.web.server.ResponseStatusException

@Tag(name = "city", description = "The City API")
@RestController
@RequestMapping("/api/city")
class CityController(private val cityRepository: CityRepository) {


    @Operation(summary = "Add city", tags = ["city"])
    @PostMapping
    @ResponseBody
    fun addCity(@RequestBody city: City): City {
        return cityRepository.save(city)
    }

    @Operation(summary = "Gets all cities", tags = ["city"])
    @GetMapping
    @ResponseStatus(OK)
    fun findCities() : List<City> = cityRepository.findAll()

}