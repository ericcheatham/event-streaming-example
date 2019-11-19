import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.amqp.scaladsl.AmqpSource
import akka.stream.alpakka.amqp.{AmqpLocalConnectionProvider, NamedQueueSourceSettings, QueueDeclaration, ReadResult}
import akka.stream.scaladsl.{Sink, Source}
import org.slf4j.LoggerFactory

object Main extends App {
  implicit val system: ActorSystem = ActorSystem("SimpleQueueExample")
  implicit val mat: ActorMaterializer = ActorMaterializer()

  val logger = LoggerFactory.getLogger(getClass)

  // Create the queue to publish to
  val bufferSize = 10
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

  val result =
    queueSource
      .take(bufferSize)
      .map({ x ⇒ x.bytes.utf8String })
      .alsoTo(Sink.foreach { x ⇒ println(s"Received message: $x") })
      .runWith(Sink.seq)
}