
fun main() {
    val yandex = Search(1000000, 1000, "Yandex")
    val google = Search(10000000, 10000, "Google")
    val tinkoff = Bank(2000000, 500, "Tinkoff")
    val apple = HiTech(342349322, 30000, "Apple")
    with(yandex) {
        printName()
        printIncome();
        updateIncome()
        printIncome();
        updateIncome(100)
        printIncome();
    }

    with(tinkoff) {
        printName()
        printIncome();
        updateIncome()
        printIncome();
    }

    with(apple) {
        printIncome();
        releaseNewIphone("Iphone-2021!")
        updateIncome()
        printIncome();
    }
    val companies = listOf(apple, tinkoff, yandex, google)
    companies.forEach{ it.printName() }
}
