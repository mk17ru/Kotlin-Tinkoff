class TableInitializator(private val dataBase: Service) {
    fun createTables() {
        dataBase.createTable(
            "`users`", "id INTEGER PRIMARY KEY", "name VARCHAR(50)",
            "surname VARCHAR(50)"
        )
        dataBase.createTable(
            "posts", "id INTEGER PRIMARY KEY", "user_id INTEGER", "text TEXT",
            "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
        )
        dataBase.createTable(
            "roles", "id INTEGER PRIMARY KEY",
            "role TEXT DEFAULT 'GUEST' CHECK(role IN ('ADMIN', 'GUEST', 'PROGRAMMER', 'MODERATOR', 'USER'))"
        )
        dataBase.createTable(
            "user_roles", "user_id INTEGER", "role_id INTEGER",
            "CONSTRAINT user_role_pk PRIMARY KEY (user_id, role_id)",
            "CONSTRAINT user_pk FOREIGN KEY (user_id) REFERENCES users (id)",
            "CONSTRAINT role_pk FOREIGN KEY (role_id) REFERENCES roles (id)"
        )
        dataBase.addUsers()
        dataBase.addPosts()
        dataBase.addRoles()
        dataBase.addUserRoles()
    }

}