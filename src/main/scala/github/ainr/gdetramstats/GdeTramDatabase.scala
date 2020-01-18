package github.ainr.gdetramstats

import cats.effect.IO
import com.typesafe.config.ConfigFactory
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor


class GdeTramDatabase {

  val conf = ConfigFactory.load("application.conf")
  val driver = conf.getString("GdeTramStats.doobie.driver")
  val url = conf.getString("GdeTramStats.doobie.url")
  val user = conf.getString("GdeTramStats.doobie.username")
  val pass = conf.getString("GdeTramStats.doobie.password")

  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

  val transactor = Transactor.fromDriverManager[IO](driver, url, user, pass)

  def maxStops(): String = {
    val program: ConnectionIO[List[String]] = sql"select name from country".query[String].to[List]
    val task: IO[List[String]] = program.transact(transactor)
    val result: List[String] = task.unsafeRunSync
    result.toString
  }

  /*
    val program: ConnectionIO[String] = sql"select name from country where code='96'".query[String].unique
    val task: IO[String] = program.transact(transactor)
    val result: String = task.unsafeRunSync
    println(s"Result ${result}")
  */

  /*
    val query : String = "country"
    val program: ConnectionIO[List[String]] = sql"select name from country".query[String].to[List]
    val task: IO[List[String]] = program.transact(transactor)
    val result: List[String] = task.unsafeRunSync
    println(s"Result ${result}")
  */
}
