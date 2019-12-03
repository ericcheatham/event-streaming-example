import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.amqp.{AmqpLocalConnectionProvider, QueueDeclaration}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import models.CustomerCreated
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization.read
import org.slf4j.LoggerFactory


object Main extends App with DatabaseMigration {

  migrate()

  implicit val system: ActorSystem = ActorSystem("SimpleQueueExample")
  system.registerOnTermination(session.close())

  implicit val mat: ActorMaterializer = ActorMaterializer()
  implicit val session = SlickSession.forConfig("slick-postgres")
  implicit val formats = Serialization.formats(NoTypeHints)

  val logger = LoggerFactory.getLogger(getClass)

  // Create the queue to publish to
  val bufferSize = 10
  val queueName = "amqp-test-queue-example"
  val queueDeclaration = QueueDeclaration(queueName)

  val connectionProvider = AmqpLocalConnectionProvider

  import session.profile.api._

  MessageQueue.createQueue(connectionProvider, queueName, queueDeclaration, bufferSize)
    .take(bufferSize)
    .map({ x => read[CustomerCreated](x.bytes.utf8String) })
    .log("Received message", x ⇒ println(x))
    .runWith(
      Slick.sink({ x ⇒ sqlu"INSERT INTO customers (name, email) VALUES(${x.name}, ${x.email})" })
    )
}