import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class TestDAO {

    @Test
    fun `test DAO with mock`() {
        val client = Client()
        val user = User(1, "Ilya", "Ryabikov")
        val list = listOf<User>()
        val dao = mockk<ServiceDB> {
        every{getClient()} returns client;
        every{findUserById(any())} returns null
        for (i in 1..4) {
            every{findUserById(i)} returns user
        }
        every{findAllUser()} returns list
        }
        assertAll("Test ServiceDB findUserById",
            { Assertions.assertEquals(user, dao.findUserById(1)) },
            { Assertions.assertEquals(user, dao.findUserById(2)) },
            { Assertions.assertEquals(user, dao.findUserById(3)) },
            { Assertions.assertEquals(user, dao.findUserById(4)) },
            { Assertions.assertEquals(null, dao.findUserById(10))},
            { Assertions.assertEquals(null, dao.findUserById(-1))},
            { Assertions.assertEquals(null, dao.findUserById(5))},
            { Assertions.assertEquals(null, dao.findUserById(0))},
            { verify(exactly = 8) {dao.findUserById(any())}}
            )
        assertAll("Test ServiceDB findAllUser",
            { Assertions.assertEquals(list, dao.findAllUser())},
            { Assertions.assertEquals(list, dao.findAllUser())},
            { Assertions.assertEquals(list, dao.findAllUser())},
            { Assertions.assertEquals(list, dao.findAllUser())},
            { verify(exactly = 4) {dao.findAllUser()}}
        )
        assertAll("Test ServiceDB getClient",
            { Assertions.assertEquals(client, dao.getClient()) },
            { verify(exactly = 1) {dao.getClient()} }
        )
    }
}