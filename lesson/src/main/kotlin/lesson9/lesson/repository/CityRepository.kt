package lesson9.lesson.repository

import lesson9.lesson.domain.City
import lesson9.lesson.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface CityRepository : JpaRepository<City, Long> {
    fun findCityById(id : Long) : City?;
    fun findCityByName(name : String) : City?
}