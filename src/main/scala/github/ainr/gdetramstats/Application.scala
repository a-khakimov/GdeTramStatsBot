package github.ainr.gdetramstats

import cats.effect.IO
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor

object Application extends App {

    implicit val cs = IO.contextShift(ExecutionContexts.synchronous)
    val transactor = Transactor.fromDriverManager[IO](
        "org.postgresql.Driver",
        "jdbc:postgresql:gdetram",
        "gdetram",
        "pass"
    )

    val program: ConnectionIO[Int] = sql"select population from country where name='Ekb'".query[Int].unique
    val task: IO[Int] = program.transact(transactor)
    val result: Int = task.unsafeRunSync
    println(s"Result ${result}")

    val bot = new ProxyTgBot("468602498:AAGLQReXfAm7ORtyp9vJCyB3Q-ABZePphQk", "localhost", 8118)
    bot.run()
    println("Press [ENTER] to shutdown the bot, it may take a few seconds...")
    scala.io.StdIn.readLine()
    bot.shutdown() // initiate shutdown
}