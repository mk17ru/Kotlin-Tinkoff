class ParserCV(cv: String) {

    private val cv : String = cv.toLowerCase()

    fun hasWord(word: String) : Boolean {
        return cv.indexOf(word.toLowerCase()) != -1

    }

    fun countWord(word : String) : Int {
        var cou = 0;
        val wordCopy = word.toLowerCase()
        for (i in 0 until cv.length - word.length) {
            if (wordCopy == cv.substring(i, i + word.length)) {
                cou++
            }
        }
        return cou
    }


}