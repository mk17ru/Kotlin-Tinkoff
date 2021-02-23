class Search(override var income: Int, override var numberOfEmployees: Int, override var name: String) : Company() {

    public override fun updateIncome() {
        income *= 3;
    }

    fun updateIncome(value : Int) {
        income = value;
    }

    override fun printName() {
        println("'${name}' finds all!")
    }
}
