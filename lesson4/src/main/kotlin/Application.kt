fun main() {
    val mikhail = User(1,"Misha", "K")
    val nikita = User(2,"Nikita", "R")

    val users = Users().getUsers()
    val posts = Posts().getPosts()
    val groupPostByUser = posts.groupBy {it.userId}
    val firstTaskList = users.map{ currentUser ->
        val obj2 = Posts().getPostsByUser(currentUser.userId)
        FullUser(currentUser.userId, currentUser.name, currentUser.surname, obj2)
    }
    println("1 Task")
    firstTaskList.forEach{ curUser ->
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
    val secondTask = firstTaskList.sortedBy { it.userName }
    secondTask.forEach { curUser ->
        with(curUser) {
            println("$userName $userSurname")
            println("UserId: $userId")
            println()
        }
    }
    println("3 Task")
    val thirdTask = firstTaskList.groupBy { it.userPosts.isNotEmpty() }
    thirdTask.forEach { curPair ->
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
    val forthTask = firstTaskList.count{ it.userPosts.isEmpty() }
    println("$forthTask users haven't posts!")
}