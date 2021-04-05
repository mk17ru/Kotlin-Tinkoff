class Bank(override var income: Int, override var numberOfEmployees: Int, override var name: String) : Company() {

    public override fun updateIncome() {
        income *= 4;

    }

    override fun printName() {
        println("The best bank is '${name}'")
    }
}
