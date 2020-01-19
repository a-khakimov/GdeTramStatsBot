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

  def numberOfAllMessages(): String = {
    val all = sql"select count(*) from journal".query[Int].unique.transact(transactor).unsafeRunSync()
    val vk = sql"select count(*) from journal where platform='vk'".query[Int].unique.transact(transactor).unsafeRunSync()
    val tg = sql"select count(*) from journal where platform='tg'".query[Int].unique.transact(transactor).unsafeRunSync()
    "All: " + all.toString + "\n    vk: " + vk.toString + "\n    tg: " + tg.toString
  }

  def top10vkUsersDay(): List[(Int, Int)] = {
    val program: ConnectionIO[List[(Int, Int)]] = sql"select user_id, count(user_id) from journal where (time::date = (current_date - INTERVAL '1 day') and platform='vk') group by user_id order by count desc limit 10".query[(Int, Int)].to[List]
    program.transact(transactor).unsafeRunSync()
  }

  def top10vkUsersWeek(): List[(Int, Int)] = {
    val program: ConnectionIO[List[(Int, Int)]] = sql"select user_id, count(user_id) from journal where (time::date < (current_date) and time::date >= (current_date - INTERVAL '7 day') and platform='vk') group by user_id order by count desc limit 10".query[(Int, Int)].to[List]
    program.transact(transactor).unsafeRunSync()
  }

  def top10stopsDay(): List[(Int, Int)] = {
    val program: ConnectionIO[List[(Int, Int)]] = sql"select stop_id, count(stop_id) from journal where (time::date = (current_date - INTERVAL '1 day')) group by stop_id order by count desc limit 10".query[(Int, Int)].to[List]
    program.transact(transactor).unsafeRunSync()
  }

  def top10stopsWeek(): List[(Int, Int)] = {
    val program: ConnectionIO[List[(Int, Int)]] = sql"select stop_id, count(stop_id) from journal where (time::date < (current_date) and time::date >= (current_date - INTERVAL '7 day')) group by stop_id order by count desc limit 10".query[(Int, Int)].to[List]
    program.transact(transactor).unsafeRunSync()
  }

  def inactiveVkUsers(): List[(String, String)] = {
    val program: ConnectionIO[List[(String, String)]] = sql"select * from (select max(time), user_id from journal where platform='vk' group by user_id order by max(time)) as foo where DATE_PART('day', now() - foo.max) > 30".query[(String, String)].to[List]
    program.transact(transactor).unsafeRunSync()
  }

  def inactiveTgUsers(): List[(String, String)] = {
    val program: ConnectionIO[List[(String, String)]] = sql"select * from (select max(time), user_id from journal where platform='tg' group by user_id order by max(time)) as foo where DATE_PART('day', now() - foo.max) > 30".query[(String, String)].to[List]
    program.transact(transactor).unsafeRunSync()
  }
}
