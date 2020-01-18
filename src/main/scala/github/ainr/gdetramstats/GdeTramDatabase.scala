package github.ainr.gdetramstats

import cats.effect.IO
import com.typesafe.config.ConfigFactory
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import shapeless.PolyDefns.->


class GdeTramDatabase {

  val conf = ConfigFactory.load("application.conf")
  val driver = conf.getString("GdeTramStats.doobie.driver")
  val url = conf.getString("GdeTramStats.doobie.url")
  val user = conf.getString("GdeTramStats.doobie.username")
  val pass = conf.getString("GdeTramStats.doobie.password")

  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)
  val transactor = Transactor.fromDriverManager[IO](driver, url, user, pass)

  def uniqueVkUsersWeek(): Int = {
    val program: ConnectionIO[Int] = sql"select count(distinct user_id) from journal where (time::date < (current_date) and time::date >= (current_date - INTERVAL '7 day') and platform='vk')".query[Int].unique
    program.transact(transactor).unsafeRunSync()
  }

  def uniqueVkUsersDay(): Int = {
    val program: ConnectionIO[Int] = sql"select count(distinct user_id) from journal where (time::date = (current_date - INTERVAL '1 day') and platform='vk')".query[Int].unique
    program.transact(transactor).unsafeRunSync()
  }

  def uniqueTgUsersWeek(): Int = {
    val program: ConnectionIO[Int] = sql"select count(distinct user_id) from journal where (time::date < (current_date) and time::date >= (current_date - INTERVAL '7 day') and platform='tg')".query[Int].unique
    program.transact(transactor).unsafeRunSync()
  }

  def uniqueTgUsersDay(): Int = {
    val program: ConnectionIO[Int] = sql"select count(distinct user_id) from journal where (time::date = (current_date - INTERVAL '1 day') and platform='tg')".query[Int].unique
    program.transact(transactor).unsafeRunSync()
  }


  def numberVkMessagesWeek(): Int = {
    val program: ConnectionIO[Int] = sql"select count(user_id) from journal where (time::date < (current_date) and time::date >= (current_date - INTERVAL '7 day') and platform='vk')".query[Int].unique
    program.transact(transactor).unsafeRunSync()
  }

  def numberVkMessagesDay(): Int = {
    val program: ConnectionIO[Int] = sql"select count(user_id) from journal where (time::date = (current_date - INTERVAL '1 day') and platform='vk')".query[Int].unique
    program.transact(transactor).unsafeRunSync()
  }

  def numberTgMessagesWeek(): Int = {
    val program: ConnectionIO[Int] = sql"select count(user_id) from journal where (time::date < (current_date) and time::date >= (current_date - INTERVAL '7 day') and platform='tg')".query[Int].unique
    program.transact(transactor).unsafeRunSync()
  }

  def numberTgMessagesDay(): Int = {
    val program: ConnectionIO[Int] = sql"select count(user_id) from journal where (time::date = (current_date - INTERVAL '1 day') and platform='tg')".query[Int].unique
    program.transact(transactor).unsafeRunSync()
  }

  /* For example
    val query : String = "country"
    val program: ConnectionIO[List[String]] = sql"select name from country".query[String].to[List]
    val task: IO[List[String]] = program.transact(transactor)
    val result: List[String] = task.unsafeRunSync
    println(s"Result ${result}")
  */
}
