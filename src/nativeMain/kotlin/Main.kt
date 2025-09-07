import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Message(
    val topic: String,
    val content: String,
)

val PrettyPrintJson = Json {
    prettyPrint = true
}

class Dummy {}

fun main() {
    val message = Message(
        topic = "Kotlin/Native",
        content = "Hello!"
    )
    println(PrettyPrintJson.encodeToString(message))
}
