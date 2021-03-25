import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class NewMethodTests {

    private val additionMethod = AdditionMethod()
    @Test
    fun testNewMethodCountWordForString() {
        with(additionMethod) {
            val s = "sssYYssshhdddsssddssdsss"
            assertEquals(1, s.countWord("sss", 0, 3));
            assertEquals(4, s.countWord("sss"))
            assertEquals(3, s.countWord("sss", 0, s.length - 1))
            assertEquals(6, s.countWord("d"))
            assertEquals(0, s.countWord("d", s.length))
            assertEquals(3, s.countWord("d", 13, s.length))
            assertEquals(1, s.countWord("dd", 12, s.length))
            assertEquals(2, s.countWord("dd", 11, s.length))
            assertEquals(0, "".countWord("a"))

        }
    }


    @Test
    fun testFailedMethodCountWordForString() {
        val s = "aasbbsafjahfjsahfaa"
        failedTest(s, "fff", s.length + 1)
        failedTest(s, "d", -1)
        failedTest("dkfkdsjfs", "fsdfs", -4324234)
        failedTest(s, "d", 423423422)
        failedTest(s, "d", 423423422, 43434)
        failedTest(s, "d", 4, 3)
        failedTest(s, "d", -3, 4)
        failedTest("fjskdffjskdffjskdffjskdf", "fjskdf", -3, -4)
        failedTest(s, "d", 3, -4)

    }

    private fun failedTest(s : String, word : String, vararg indexes : Int) {
        with(additionMethod) {
            val exception = assertThrows(IllegalArgumentException::class.java) {
                when(indexes.size) {
                    0 -> s.countWord(word)
                    1 -> s.countWord(word, indexes[0])
                    2 -> s.countWord(word, indexes[0], indexes[1])
                    else -> throw IllegalArgumentException("Test has incorrect numbers of arguments:" + indexes.size)
                }
            }
            assertEquals("Wrong arguments startIndex or endIndex", exception.message)
        }
    }

}