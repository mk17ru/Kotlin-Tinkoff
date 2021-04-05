data class FullUser(val userId: Int = 0, val userName: String, val userSurname: String,
                    val userPosts : List<Post>, val userRoles : List<Role>)