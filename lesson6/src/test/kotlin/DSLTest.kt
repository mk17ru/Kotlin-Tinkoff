import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class DSLTest {

    private fun createMock(user : User, client : Client, ) {


    }

    @Test
    fun `Default values should be returned`() {
        val testOffice = MiniOfficeDSL("Boss", "Svetlana")
        testOffice.orderCoffee()
        val office = mockk<MiniOfficeDSL> {
            every { orderCoffee() } returns true
            every { getMoney()} returns 100
            every { getCoffeePackets() } returns 10
            every { getBossName() } returns "Ilya"
            every { secretaryName } returns "Svetlana"
            every { buyCoffee(any())} returns false
            every{MiniOfficeDSL {orderCoffee()}}
        }
        assertAll(,
            {assertEquals(true, office.orderCoffee())},
            {assertEquals(100, office.getMoney())},
            {assertEquals(10, office.getCoffeePackets())},
            {assertEquals("Ilya", office.getBossName())},
            {assertEquals("Svetlana", office.secretaryName)},

            {assertEquals(false, office.buyCoffee(2)) },
            {assertEquals(testOffice.getCoffeePackets(),
                                        MiniOfficeDSL("Boss", "Svetlana").getCoffeePackets())}
        )
    }


 }