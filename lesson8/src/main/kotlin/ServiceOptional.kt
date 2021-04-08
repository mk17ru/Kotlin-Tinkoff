import kotlin.random.Random

class ServiceOptional() {

    private val shift = 5;

    fun encryptPassword(password : String) : String {
        val sb = StringBuilder();
        sb.append(password)
        for (i in sb.indices) {
            sb[i] = (sb[i] + shift)
        }
        return sb.toString()
    }

    fun decryptionPassword(password : String) : String {
        val sb = StringBuilder();
        sb.append(password)
        for (i in sb.indices) {
            sb[i] = (sb[i] - shift)
        }
        return sb.toString()
    }

    fun generateString() : String {
        val s = StringBuilder()
        val size = Random.nextInt()
        for (i in 1..size) {
            s.append(Random.nextInt())
        }
        return s.toString()
    }

    suspend fun generatePassword(): String {
        return encryptPassword(generatePassword())
    }


}