package github.ainr.gdetramstats

import cats.instances.future._
import cats.syntax.functor._
import com.bot4s.telegram.api.declarative.Commands
import com.bot4s.telegram.clients.ScalajHttpClient
import com.bot4s.telegram.future.Polling
import com.bot4s.telegram.methods.SendMessage
import com.bot4s.telegram.models.Message

import scala.concurrent.Future

class GdeTramStatsTgBot(token: String) extends TgBot(token)
  with Polling
  with Commands[Future] {

  val gdetramdb = new GdeTramDatabase
  override val client = new ScalajHttpClient(token)

  onCommand("/uniqueVkUsersDay")     { implicit msg => reply(gdetramdb.uniqueVkUsersDay().toString).void     }
  onCommand("/uniqueVkUsersWeek")    { implicit msg => reply(gdetramdb.uniqueVkUsersWeek().toString).void    }
  onCommand("/uniqueTgUsersDay")     { implicit msg => reply(gdetramdb.uniqueTgUsersDay().toString).void     }
  onCommand("/uniqueTgUsersWeek")    { implicit msg => reply(gdetramdb.uniqueTgUsersWeek().toString).void    }

  onCommand("/numberVkMessagesDay")  { implicit msg => reply(gdetramdb.numberVkMessagesDay().toString).void  }
  onCommand("/numberVkMessagesWeek") { implicit msg => reply(gdetramdb.numberVkMessagesWeek().toString).void }
  onCommand("/numberTgMessagesDay")  { implicit msg => reply(gdetramdb.numberTgMessagesDay().toString).void  }
  onCommand("/numberTgMessagesWeek") { implicit msg => reply(gdetramdb.numberTgMessagesWeek().toString).void }

  onCommand("/numberOfAllMessages") { implicit msg => reply(gdetramdb.numberOfAllMessages()).void }

  onCommand("/top10vkUsersDay") {
    val top = gdetramdb.top10vkUsersDay().map(
      user => ("https://vk.com/id" + user._1.toString() + " sent " + user._2.toString + " messages\n")
    ).fold("")(_ + _)
    implicit msg => reply(top).void
  }

  onCommand("/top10vkUsersWeek") {
    val top = gdetramdb.top10vkUsersWeek().map(
      user => ("https://vk.com/id" + user._1.toString() + " sent " + user._2.toString + " messages\n")
    ).fold("")(_ + _)
    implicit msg => reply(top).void
  }

  onCommand("/top10stopsDay") {
    val top = gdetramdb.top10stopsDay().map(
      a => ("[" + a._2.toString() + "] " + a._1.toString + "\n")
    ).fold("")(_ + _)
    implicit msg => reply(top).void
  }

  onCommand("/top10stopsWeek") {
    val top = gdetramdb.top10stopsWeek().map(
      a => ("[" + a._2.toString() + "] " + a._1.toString + "\n")
    ).fold("")(_ + _)
    implicit msg => reply(top).void
  }

  onCommand("/inactiveVkUsers") {
    val top = gdetramdb.inactiveVkUsers().map(
      a => ("[" + a._1.toString() + "] https://vk.com/id" + a._2.toString + "\n")
    ).fold("")(_ + _)
    implicit msg => reply(top).void
  }

  onCommand("/inactiveTgUsers") {
    val top = gdetramdb.inactiveTgUsers().map(
      a => ("[" + a._1.toString() + "] " + a._2.toString + "\n")
    ).fold("")(_ + _)
    implicit msg => reply(top).void
  }

  onCommand("help") { implicit msg =>
    val help = "" +
      "/help\n" +
      "\n" +
      "/uniqueVkUsersDay - Количество уникальных пользователей вк за сутки\n" +
      "/uniqueVkUsersWeek - Количество уникальных пользователей вк за неделю\n" +
      "/uniqueTgUsersDay - Количество уникальных пользователей телеграм за сутки\n" +
      "/uniqueTgUsersWeek - Количество уникальных пользователей телеграм за неделю\n" +
      "\n" +
      "/numberVkMessagesDay - Общее количество сообщений вк за сутки\n" +
      "/numberVkMessagesWeek - Общее количество сообщений вк за неделю\n" +
      "/numberTgMessagesDay - Общее количество сообщений телеграм за сутки\n" +
      "/numberTgMessagesWeek - Общее количество сообщений телеграм за неделю\n" +
      "/numberOfAllMessages - Количество всех сообщений\n" +
      "\n" +
      "/top10vkUsersDay - Топ 10 пользователей вк по количеству запросов за сутки\n" +
      "/top10vkUsersWeek - Топ 10 пользователей вк по количеству запросов за неделю\n" +
      "/inactiveVkUsers - Список пользователей вк, которые не писали боту в течении 30 дней\n" +
      "/inactiveTgUsers - Список пользователей телеграм, которые не писали боту в течении 30 дней\n" +
      "\n"
    reply(help).void
  }

  /* For example
  override def receiveMessage(msg: Message): Future[Unit] =
    msg.text.fold(Future.successful(())) { text =>
      request(SendMessage(msg.source, "")).void
    }
  */
}
