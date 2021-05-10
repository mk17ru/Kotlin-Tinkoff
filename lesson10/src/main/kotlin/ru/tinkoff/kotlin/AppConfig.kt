package ru.tinkoff.kotlin

data class AppConfig(
    val http: HttpConfig,
    val database : DataBaseConfig
)

data class HttpConfig(val port : Int)

data class DataBaseConfig(val url : String, val user : String, val password : String)