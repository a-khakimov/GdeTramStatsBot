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

  onCommand("uniqueVkUsersDay")  { implicit msg => reply(gdetramdb.uniqueVkUsersDay().toString).void }
  onCommand("uniqueVkUsersWeek") { implicit msg => reply(gdetramdb.uniqueVkUsersWeek().toString).void }
  onCommand("uniqueTgUsersDay")  { implicit msg => reply(gdetramdb.uniqueTgUsersDay().toString).void }
  onCommand("uniqueVkUsersWeek") { implicit msg => reply(gdetramdb.uniqueTgUsersWeek().toString).void }

  onCommand("numberVkMessagesDay")  { implicit msg => reply(gdetramdb.numberVkMessagesDay().toString).void }
  onCommand("numberVkMessagesWeek") { implicit msg => reply(gdetramdb.numberVkMessagesWeek().toString).void }
  onCommand("numberTgMessagesDay")  { implicit msg => reply(gdetramdb.numberTgMessagesDay().toString).void }
  onCommand("numberTgMessagesWeek") { implicit msg => reply(gdetramdb.numberTgMessagesWeek().toString).void }

  onCommand("help") { implicit msg =>
    val help = "" +
      "/help\n" +
      "\n" +
      "/uniqueVkUsersDay - Количество уникальных пользователей в вк за сутки\n" +
      "/uniqueVkUsersWeek - Количество уникальных пользователей в вк за неделю\n" +
      "/uniqueTgUsersDay - Количество уникальных пользователей в телеграм за сутки\n" +
      "/uniqueVkUsersWeek - Количество уникальных пользователей в телеграм за неделю\n" +
      "\n" +
      "/numberVkMessagesDay - Общее количество сообщений в вк за сутки\n" +
      "/numberVkMessagesWeek - Общее количество сообщений в вк за неделю\n" +
      "/numberTgMessagesDay - Общее количество сообщений в телеграм за сутки\n" +
      "/numberTgMessagesWeek - Общее количество сообщений в телеграм за неделю\n" +
      "\n" +
      ""
    reply(help).void
  }
/*
  override def receiveMessage(msg: Message): Future[Unit] =
    msg.text.fold(Future.successful(())) { text =>
      request(SendMessage(msg.source, "")).void
    }

 */
}
