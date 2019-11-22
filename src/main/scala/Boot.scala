import akka.stream.alpakka.slick.javadsl.SlickSession
import org.slf4j.{Logger, LoggerFactory}

class Boot extends App {

  val logger = LoggerFactory.getLogger(getClass)


  val postgresSession: SlickSession = SlickSession.forConfig("slick-postgres")
}
