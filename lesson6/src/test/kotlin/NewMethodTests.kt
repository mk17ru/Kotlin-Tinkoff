import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.lang.IllegalArgumentException

class NewMethodTests {

    private val additionMethod = AdditionMethod()
    private val stringSSS = "sssYYssshhdddsssddssdsss"

    @Test
    fun `Count Words For String in the start`() {
        with(additionMethod) {
            assertEquals(1, stringSSS.countWord("sss", 0, 3))
            assertEquals(2, stringSSS.countWord("sss", 0, 9))
            assertEquals(0, stringSSS.countWord("sss", 0, 0))
        }
    }

    @Test
    fun `Count Words For String in the end`() {
        with(additionMethod) {
            with (stringSSS) {
                assertEquals(1, countWord("sss", length - 3))
                assertEquals(1, countWord("sss", length - 6, length))
                assertEquals(0, countWord("d", length))
                assertEquals(1, countWord("s", length - 1, length))
            }
        }
    }

    @Test
    fun `Count Words For StringSSS basic tests`() {
        with(additionMethod) {
            with (stringSSS) {
                assertEquals(4, countWord("sss"))
                assertEquals(3, countWord("sss", 0, length - 1))
                assertEquals(6, countWord("d"))
                assertEquals(3, countWord("d", 13, length))
                assertEquals(1, countWord("dd", 12, length))
                assertEquals(2, countWord("dd", 11, length))
            }
        }
    }

    @Test
    fun `Count Words For random strings`() {
        with(additionMethod) {
            assertEquals(3, "xdlfldskfsdxdkslkxd".countWord("xd"))
            assertEquals(2, "xdlfldskfsdsxdkslsds".countWord("sds", 0))
            assertEquals(0, "daaababbabbsssbab".countWord("d", 1))
            assertEquals(1, "fdsfjddskfds".countWord("dd", 3, 12))
            assertEquals(1, "xdlfldskfsdxddslks".countWord("dd", 9, 18))
            assertEquals(0, "".countWord("a"))
        }
    }

    @Test
    fun testFailedMethodCountWordForString() {
        val s = "aasbbsafjahfjsahfaa"
        failedTest(s, "fff", s.length + 1)
        failedTest(s, "d", -1)
        failedTest(s, "d", 4, 3)
        failedTest("Happyd", "d", -3, 4)
        failedTest("fjskdffjskdffjskdffjskdf", "fjskdf", -3, -4)
        failedTest("ddddd", "d", 3, -4)
    }

    @Test
    fun `Count words in String big values error test`() {
        val s = "aasbbsafjahfjsahfaa"
        failedTest("dkfkdsjfs", "fsdfs", -4324234)
        failedTest("dkfkdsjfs", "fsdf", 1, -55334234)
        failedTest("dkfkdsjfs", "fsdfs", 1, 2000000000)
        failedTest(s, "d", 423423422)
        failedTest(s, "d", 423423422, 43434)
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