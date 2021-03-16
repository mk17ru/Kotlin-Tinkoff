fun main() {
    val mikhail = User(1,"Misha", "K")
    val nikita = User(2,"Nikita", "R")
    val server = Server(UsersDAO(), PostsDAO())
    println("1 Task")
    server.getFullUsers().forEach{ curUser ->
        with(curUser) {
            println("$userName $userSurname")
            println("UserId: $userId")
            if (userPosts.isNotEmpty()) {
                println("Posts---------->")
                println()
            } else {
                println("No posts :(((((((")
            }
            userPosts.forEach {
                println("PostId " + it.postId)
                println(it.text)
                println()
            }
        }
        println("-------------------------------------------")
    }
    println("2 Task")
    server.getSortedFullUsers().forEach { curUser ->
        with(curUser) {
            println("$userName $userSurname")
            println("UserId: $userId")
            println()
        }
    }
    println("3 Task")
    server.groupFullUsersByPostsExist().forEach { curPair ->
        if (curPair.key) {
            println("They have posts:")
        } else {
            println("They haven't posts:")

        }
        println()
        curPair.value.forEach { curUser ->
            with(curUser) {
                println("$userName $userSurname")
                println("UserId: $userId")
                println()
            }
        }
    }
    println("4 Task")
    println(server.countFullUsersWithoutPosts().toString() + " users haven't posts!")
}