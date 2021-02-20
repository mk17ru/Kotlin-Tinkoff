class HiTech(override var income: Int, override var numberOfEmployees: Int, override var name: String) : Company() {

    private var currentIphoneModel : String = "IPhone 12"
    private val iosVersion : Float = 1.1F

    fun releaseNewIphone(newModel : String) {
        println("Old Model $currentIphoneModel")
        currentIphoneModel = newModel
        println("Current Model $newModel")
    }

    public override fun updateIncome() {
        income *= 2;
    }

}