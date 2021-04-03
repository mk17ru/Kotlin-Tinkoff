class Producer(private val DataBase : DataBase) {
    fun set(data : String) {
        while(!DataBase.endingReading) {
            synchronized(this) {
                if (DataBase.data == null) {
                    DataBase.data = data
                    return
                }
            }
        }
    }
}