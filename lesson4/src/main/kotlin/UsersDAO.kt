class UsersDAO {
    private val users : List<User> = listOf(
            User(1, "Mikhail", "Kazakov"),
            User(2, "Pasha", "Terentiev"),
            User(3, "Kolya", "Shipyakov"),
            User(4, "Anna", "Labazova"),
            User(5, "Artem", "Shalito"),
            User(10, "Ilya", "Abramov")
    )

    @Override
    fun getUsers() : List<User> {
        return users
    }
}