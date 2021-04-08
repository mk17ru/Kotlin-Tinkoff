import java.lang.Math.abs
import kotlin.random.Random

class ServiceOptional() {

    private val shift = 5;

    private suspend fun encryptPassword(password : String) : String {
        val sb = StringBuilder();
        sb.append(password)
        for (i in sb.indices) {
            sb[i] = (sb[i] + shift)
        }
        return sb.toString()
    }

    suspend fun decryptionPassword(password : String) : String {
        val sb = StringBuilder();
        sb.append(password)
        for (i in sb.indices) {
            sb[i] = (sb[i] - shift)
        }
        return sb.toString()
    }

    suspend fun generateString() : String {
        val s = StringBuilder()
        val size = (kotlin.math.abs(Random.nextInt())) % 25 + 5
        for (i in 1..size) {
            s.append((kotlin.math.abs(Random.nextInt()) % 250 + 1).toChar().toString())
        }
        return s.toString()
    }

    suspend fun generatePassword(): String {
        return encryptPassword(generateString())
    }


}