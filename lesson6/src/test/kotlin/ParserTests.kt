import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
class ParserTests {

    @Test
    fun testParserCountWord() {
        val parser = ParserCV("I know Python JAVA KOTLIN C++. And I also love Kotlin! ")
        assertEquals(2, parser.countWord("Kotlin"))
        assertEquals(2, parser.countWord("KOTLIN"))
        assertEquals(55, parser.countWord(""))
        assertEquals(1, parser.countWord("JAVa"))
        assertEquals(1, parser.countWord("jAVa"))
        assertEquals(0, parser.countWord("C++11"))
        assertEquals(0, parser.countWord("C#"))
    }

    @Test
    fun testParserHasWord() {
        val parser = ParserCV("I know Python JAVA KOTLIN C++. Kotlin! ")
        assertEquals(true, parser.hasWord("Kotlin"))
        assertEquals(false, parser.hasWord("OOP"))
        assertEquals(true, parser.hasWord("C++"))
        assertEquals(false, parser.hasWord("Javac"))
    }

    @Test
    fun testParserInit() {
        assertEquals(1, ParserCV("I know Python JAVA KOTLIN C++. Kotlin! ").countWord("Java"))
        assertEquals(0, ParserCV("").countWord("Java"))
        assertEquals(0, ParserCV("!!!!!!!!!!!").countWord("Java"))
        assertEquals(1, ParserCV("Javaaaa").countWord("Java"))
    }
}