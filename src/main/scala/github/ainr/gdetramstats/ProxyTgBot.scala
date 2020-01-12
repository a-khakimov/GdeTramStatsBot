package github.ainr.gdetramstats


import java.net.{InetSocketAddress, Proxy}

import cats.instances.future._
import cats.syntax.functor._
import com.bot4s.telegram.api.declarative.Commands
import com.bot4s.telegram.clients.ScalajHttpClient
import com.bot4s.telegram.future.Polling
import com.bot4s.telegram.api.RequestHandler
import com.bot4s.telegram.clients.FutureSttpClient
import com.bot4s.telegram.future.TelegramBot
import com.bot4s.telegram.methods.SendMessage
import com.bot4s.telegram.models.Message
import slogging.{LogLevel, LoggerConfig, PrintLoggerFactory}
import com.softwaremill.sttp.okhttp._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class ProxyTgBot(token: String, host: String, port: Int) extends TgBot(token)
  with Polling
  with Commands[Future] {

  val proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(host, port))

  override val client = new ScalajHttpClient(token, proxy)

  onCommand("dice" | "roll") { implicit msg =>
    reply("Hi").void
  }

  override def receiveMessage(msg: Message): Future[Unit] =
    msg.text.fold(Future.successful(())) { text =>
      request(SendMessage(msg.source, text.reverse)).void
    }

}
