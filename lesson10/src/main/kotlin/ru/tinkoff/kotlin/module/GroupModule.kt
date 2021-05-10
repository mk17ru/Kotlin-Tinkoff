package ru.tinkoff.kotlin.plugins

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

fun Application.groupModule() {
    val groupService : GroupService by closestDI().instance()
    routing {
        route("/groups") {
            get {
                call.respond(groupService.findAll())
            }
            post("/addToGroup") {
                val request = call.receive<CreateGroupRequest>()
                call.respond(groupService.addToGroup(request.number, request.students))
            }
        }
    }
}
@Serializable
private data class CreateGroupRequest(val number : Int, val students: List<Student>)

fun DI.Builder.GroupComponents() {
    bind<GroupDao>() with singleton { GroupDao(instance(), instance()) }
    bind<GroupService>() with singleton { GroupService(instance()) }
}