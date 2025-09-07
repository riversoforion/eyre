import kotlin.test.Test
import kotlin.test.assertEquals

class PrettyPrintJsonTest {
    @Test
    fun testPrettyPrintJson() {
        val input = Message(
            topic = "JsonTest",
            content = "I'm working!"
        )
        val expected = """
            {
                "topic": "JsonTest",
                "content": "I'm working!"
            }
        """.trimIndent()
        val actual = PrettyPrintJson.encodeToString(input)
        assertEquals(expected, actual)
    }

    @Test
    fun `oh noes! a failing test`() {
        assertEquals(1, 2)
    }
}
