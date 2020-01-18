package github.ainr.gdetramstats

import com.typesafe.config.ConfigFactory

object Application extends App {

  val conf = ConfigFactory.load("application.conf")
  val token = conf.getString("GdeTramStats.telegram.token")
  val proxyStatus = conf.getString("GdeTramStats.telegram.proxyStatus")
  val host = conf.getString("GdeTramStats.telegram.proxy.host")
  val port = conf.getInt("GdeTramStats.telegram.proxy.port")

  val bot = if (proxyStatus == "enable") {
    new GdeTramStatsTgProxyBot(token, host, port)
  } else {
    new GdeTramStatsTgBot(token)
  }

  bot.run()
  println("Press [ENTER] to shutdown the bot, it may take a few seconds...")
  scala.io.StdIn.readLine()
  bot.shutdown() // initiate shutdown
}