import java.lang.IllegalArgumentException

class AdditionMethod {

    /*
        find word in String
        throw Illegal argument exception
        if endIndex > this.length  || startIndex > this.length || startIndex < 0 || endIndex < 0 || startIndex > endIndex
    */


    fun String.countWord(word : String, startIndex : Int = 0, endIndex : Int = this.length) : Int {
        if (endIndex > this.length  || startIndex > this.length
            || startIndex < 0 || endIndex < 0 || startIndex > endIndex) {
            throw IllegalArgumentException("Wrong arguments startIndex or endIndex");
        }
        var index = -1
        var cou = 0
        do {
            index = this.indexOf(word, index + 1)
            if (index == -1) {
                break
            }
            if (index >= startIndex && index + word.length - 1 < endIndex) {
                cou++
            }
        } while (index != -1 && index < endIndex)
        return cou
    }

}