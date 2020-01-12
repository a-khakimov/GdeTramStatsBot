package example

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

object SttpBackends {
  val default = OkHttpFutureBackend()
}

abstract class ExampleBot(val token: String) extends TelegramBot {
  LoggerConfig.factory = PrintLoggerFactory()
  LoggerConfig.level = LogLevel.TRACE

  implicit val backend = SttpBackends.default
  override val client: RequestHandler[Future] = new FutureSttpClient(token)
}

class ProxyBot(token: String) extends ExampleBot(token)
  with Polling
  with Commands[Future] {

  val proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved("localhost", 8118))

  override val client = new ScalajHttpClient(token, proxy)

  onCommand("dice" | "roll") { implicit msg =>
    reply("Hi").void
  }

  override def receiveMessage(msg: Message): Future[Unit] =
    msg.text.fold(Future.successful(())) { text =>
      request(SendMessage(msg.source, text.reverse)).void
  }

}

object Hello extends App {
  val bot = new ProxyBot("468602498:AAGLQReXfAm7ORtyp9vJCyB3Q-ABZePphQk")
  bot.run()
  println("Press [ENTER] to shutdown the bot, it may take a few seconds...")
  scala.io.StdIn.readLine()
  bot.shutdown() // initiate shutdown
}
