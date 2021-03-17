import java.util.function.BiPredicate
import java.util.function.Predicate

class Server(val users: UsersDAO, val posts: PostsDAO) {
    fun getFullUsers() : List<FullUser> {
        return users.getUsers().map{ currentUser ->
            val obj2 = PostsDAO().getPostsByUser(currentUser.userId)
            FullUser(currentUser.userId, currentUser.name, currentUser.surname, obj2)
        }
    }

    fun getSortedFullUsers() : List<FullUser> {
        return getFullUsers().sortedBy { it.userName };
    }

    fun groupFullUsersByPostsExist() : Map<Boolean, List<FullUser>> {
        return getFullUsers().groupBy { it.userPosts.isNotEmpty() }
    }

    fun countFullUsersByPredicate(predicate: Predicate<FullUser>) : Int {
        return getFullUsers().count{ predicate.test(it) }
    }
}
