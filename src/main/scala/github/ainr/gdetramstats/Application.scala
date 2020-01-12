package github.ainr.gdetramstats

object Application extends App {
    val bot = new ProxyTgBot("468602498:AAGLQReXfAm7ORtyp9vJCyB3Q-ABZePphQk", "localhost", 8118)
    bot.run()
    println("Press [ENTER] to shutdown the bot, it may take a few seconds...")
    scala.io.StdIn.readLine()
    bot.shutdown() // initiate shutdown

}