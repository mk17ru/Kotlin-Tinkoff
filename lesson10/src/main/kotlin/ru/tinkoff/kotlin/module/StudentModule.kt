package ru.tinkoff.kotlin.module

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import kotlinx.serialization.Serializable
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.kodein.di.singleton
import ru.tinkoff.kotlin.Group.service.GroupService
import ru.tinkoff.kotlin.student.dao.GroupDao
import ru.tinkoff.kotlin.student.model.Student
import ru.tinkoff.kotlin.student.dao.StudentDao
import ru.tinkoff.kotlin.student.service.StudentService

fun Application.studentModule() {
    val studentService : StudentService by closestDI().instance()
    routing {
        route("/api/students") {
            get {
                call.respond(studentService.findAll())
            }
            get("/{id}") {
                call.respond(studentService.find(Integer.parseInt(call.parameters["id"])))
            }
            put("/{id}") {
                val request = call.receive<CreateStudentRequest>()
                val id = Integer.parseInt(call.parameters["id"])
                studentService.update(
                    id, Student(
                        id,
                        request.name,
                        request.surname,
                        request.group
                    )
                )
            }
            delete("/{id}") {
                studentService.delete(Integer.parseInt(call.parameters["id"]))
            }
            put("/changeGroup/{id}") {
                val request = call.receive<ChangeStudentGroup>()
                val id = Integer.parseInt(call.parameters["id"])
                studentService.changeGroup(id, request.group)
            }
            post {
                val request = call.receive<CreateStudentRequest>()
                call.respond(studentService.create(request.name, request.surname, request.group))
            }

            get("/group/{groupId}") {
                val number = Integer.parseInt(call.parameters["number"])
                call.respond(studentService.findStudentsByGroup(number))
            }
        }
    }
}
@Serializable
private data class  CreateStudentRequest(val name : String, val surname : String, val group : Int)

@Serializable
private data class ChangeStudentGroup(val group : Int)

fun DI.Builder.StudentComponents() {
    bind<StudentDao>() with singleton { StudentDao(instance()) }
    bind<StudentService>() with singleton { StudentService(instance()) }
}
