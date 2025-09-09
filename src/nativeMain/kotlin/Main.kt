import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

fun main() {
    val message = Message(
        topic = "Kotlin/Native",
        content = "Hello!"
    )
    println(PrettyPrintJson.encodeToString(message))
}

@Serializable
data class Message(
    val topic: String,
    val content: String,
)

val PrettyPrintJson = Json {
    prettyPrint = true
}
