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

  onCommand("max" | "roll") { implicit msg =>
    val r = gdetramdb.maxStops()
    reply(r).void
  }

  onCommand("help") { implicit msg =>
    val help = "" +
      "/help" +
      "/max - description"
    reply(help).void
  }

  override def receiveMessage(msg: Message): Future[Unit] =
    msg.text.fold(Future.successful(())) { text =>
      request(SendMessage(msg.source, "")).void
    }
}
