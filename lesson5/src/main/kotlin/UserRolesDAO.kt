class UserRolesDAO {
    private val roles : List<UserRole> = listOf(
        UserRole(1, 1, Role.ADMIN),
        UserRole(2, 1, Role.MODERATOR),
        UserRole(3, 2, Role.MODERATOR),
        UserRole(4, 3, Role.PROGRAMMER),
        UserRole(5, 3, Role.USER),
        UserRole(6, 4, Role.USER),
        UserRole(7, 5, Role.ADMIN),
        UserRole(8, 10, Role.GUEST)
    )

    @Override
    fun getUserRoles() : List<UserRole> {
        return roles
    }
};