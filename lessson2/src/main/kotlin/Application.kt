
fun main() {
    val yandex = Search(1000000, 1000, "Yandex")
    val google = Search(10000000, 10000, "Google")
    val tinkoff = Bank(2000000, 500, "Tinkoff")
    val apple = HiTech(342349322, 30000, "Apple")

    with(yandex) {
        printName()
        println("Income " + getIncome())
        updateIncome()
        println("Income " + getIncome())
        updateIncome(100)
        println("Income " + getIncome())
    }

    with(tinkoff) {
        printName()
        println("Income " + getIncome())
        updateIncome()
        println("Income " + getIncome())
    }

    with(apple) {
        println("Income " + getIncome())
        releaseNewIphone("Iphone-2021!")
    }
    val companies = listOf(apple, tinkoff, yandex, google)
    companies.forEach{ it.printName() }
}
