import com.github.ajalt.clikt.command.main
import com.github.ajalt.clikt.core.subcommands
import com.riversoforion.eyre.cmd.AddCommand
import com.riversoforion.eyre.cmd.EyreCommand
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    runBlocking {
        EyreCommand()
            .subcommands(AddCommand())
            .main(args)
    }
}
