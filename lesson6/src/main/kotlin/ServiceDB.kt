class ServiceDB(private val client : Client) {

    fun getClient(): Client {
        return client
    }

    fun findUserById(id : Int) : User? {
        return client.findUserById(id)
    }

    fun findAllUser(): List<User> {
        return client.findAllUser()
    }
}