class MiniOfficeDSL(private val bossName : String, val secretaryName : String,
                    private var money : Int = 100, private var coffeePackets : Int = 10) {

    private var coffeePrice = 20

    fun setCoffeePrice(price : Int) {
        coffeePrice = price
    }

    fun getBossName(): String {
        return bossName
    }

    fun getMoney() : Int {
        return money
    }

    fun getCoffeePackets() : Int {
        return money
    }


    fun orderCoffee() : Boolean {
        return if (coffeePackets > 0) {
            coffeePackets--
            println("$coffeePackets left")
            true
        } else {
            println("No coffee")
            false
        }
    }

    fun buyCoffee(num : Int) : Boolean {
        return if (money < coffeePrice * num) {
            print("No enough money. Money: $money")
            false
        } else {
            money -= coffeePrice * num
            coffeePackets += num
            print("Buy $num coffee packets")
            true
        }
    }

    fun MiniOfficeDSL(block : MiniOfficeDSL.() -> Unit) {
        return this.block()
    }

}