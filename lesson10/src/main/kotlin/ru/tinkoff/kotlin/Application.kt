package ru.tinkoff.kotlin

import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.tinkoff.kotlin.plugins.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.ktor.di
import org.kodein.di.singleton
import ru.tinkoff.kotlin.module.StudentComponents
import ru.tinkoff.kotlin.module.studentModule


fun main() {
    val config = ConfigFactory.load().extract<AppConfig>()
    migrate(config.database)
    val embeddedServer = embeddedServer(Netty, port = config.http.port) {
        di {
            coreComponent(config)
            StudentComponents()
            GroupComponents()
        }
        studentModule()
        groupModule()
    }
    embeddedServer.start(wait = true)
}

fun DI.Builder.coreComponent(config : AppConfig) {
    bind<AppConfig>() with singleton { config }
    bind<Database>() with singleton {
        Database.connect(
            url = config.database.url,
            user = config.database.user,
            password = config.database.password
        )
    }
}

fun migrate(database: DataBaseConfig) {
    Flyway
        .configure()
        .dataSource(database.url, database.user, database.password)
        .load()
        .migrate()
}