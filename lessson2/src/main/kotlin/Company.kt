
abstract class Company {

    protected abstract var income : Int
    protected abstract val numberOfEmployees : Int
    protected abstract val name : String

    open fun printName() {
        println("Name '${name}'")
    }

    @JvmName("getIncome1")
    fun getIncome(): Int {
        return income
    }

    protected abstract fun updateIncome();
}