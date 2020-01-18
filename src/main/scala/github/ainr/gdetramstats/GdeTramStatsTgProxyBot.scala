package github.ainr.gdetramstats

import java.net.{InetSocketAddress, Proxy}
import com.bot4s.telegram.api.declarative.Commands
import com.bot4s.telegram.clients.ScalajHttpClient
import com.bot4s.telegram.future.Polling
import scala.concurrent.{Future}

class GdeTramStatsTgProxyBot(token: String, host: String, port: Int)
  extends GdeTramStatsTgBot(token)
    with Commands[Future]
    with Polling {
  val proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(host, port))
  override val client = new ScalajHttpClient(token, proxy)
}
