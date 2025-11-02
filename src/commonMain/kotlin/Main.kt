import com.github.ajalt.clikt.command.main
import com.github.ajalt.clikt.core.subcommands
import com.riversoforion.eyre.cmd.Add
import com.riversoforion.eyre.cmd.Eyre
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    runBlocking {
        Eyre()
            .subcommands(Add())
            .main(args)
    }
}
