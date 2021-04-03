class Consumer(private val DataBase : DataBase) {
    fun get() : String? {
        while(!DataBase.endingReading) {
            synchronized(this) {
                if (DataBase.data != null) {
                    val d = DataBase.data
                    DataBase.data = null
                    return d
                }
            }
        }
        return null
    }

}