class PostsDAO {

    private val posts : List<Post> = listOf(
            Post(1, 1, "Доброе утро!", listOf(2)),
            Post(2, 2, "Приглашаю всех на встречу!", listOf(1, 3)),
            Post(1, 3, "I love Russia!", listOf(4)),
            Post(4, 4, "Подскажите какие кроссовки купить?", listOf(5, 6, 7)),
            Post(4, 5, "Hi London!", emptyList()),
            Post(3, 6, "Первый день на Байкале!", listOf(8))

    )

    @Override
    fun getComments() : List<Comment> {
        return comments
    }

    @Override
    fun getPosts() : List<Post> {
        return posts
    }

    @Override
    fun getPostsByUser(userId : Int) : List<Post> {
        return posts.filter { it.userId == userId }
    }

    private val comments : List<Comment> = listOf(
            Comment(1, 2, 1,"Awesome idea!"),
            Comment(1, 1, 2,"Уже день!"),
            Comment(5, 2, 3,"Куда пойдем))!"),
            Comment(10, 3, 4,"Me too!"),
            Comment(3, 4, 5,"Nike!"),
            Comment(2, 4, 6,"Adidas!"),
            Comment(4, 6, 7,"Reebok!"),
            Comment(4, 7,8,"Dream!"),
            )

}