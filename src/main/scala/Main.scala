import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.amqp.scaladsl.AmqpSource
import akka.stream.alpakka.amqp.{AmqpLocalConnectionProvider, NamedQueueSourceSettings, QueueDeclaration, ReadResult}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.Source
import models.CustomerCreated
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization.read
import org.slf4j.LoggerFactory


object Main extends App {

  implicit val system: ActorSystem = ActorSystem("SimpleQueueExample")
  system.registerOnTermination(session.close())

  implicit val mat: ActorMaterializer = ActorMaterializer()
  implicit val session = SlickSession.forConfig("slick-postgres")
  implicit val formats = Serialization.formats(NoTypeHints)

  val logger = LoggerFactory.getLogger(getClass)

  // Create the queue to publish to
  val bufferSize = 1
  val queueName = "amqp-conn-it-test-simple-queue-example"
  val queueDeclaration = QueueDeclaration(queueName)

  val connectionProvider = AmqpLocalConnectionProvider

  val queueSource: Source[ReadResult, NotUsed] =
    AmqpSource.atMostOnceSource(
      NamedQueueSourceSettings(connectionProvider, queueName)
        .withDeclaration(queueDeclaration)
        .withAckRequired(true),
      bufferSize = bufferSize
    )

  import session.profile.api._

  val result =
    queueSource
      .take(bufferSize)
      .map({ x ⇒ read[CustomerCreated](x.bytes.utf8String) })
      .log("Received message", x ⇒ println(x))
      .runWith(
        Slick.sink({ x ⇒ sqlu"INSERT INTO customers VALUES(${x.id}, ${x.name}, ${x.email})" })
      )
}