import akka.Done
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.amqp.scaladsl.AmqpSink
import akka.stream.alpakka.amqp.{AmqpLocalConnectionProvider, AmqpWriteSettings, QueueDeclaration}
import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString

import scala.concurrent.Future


object Main extends App {
  implicit val system: ActorSystem = ActorSystem("SimpleQueueExample")
  implicit val mat: ActorMaterializer = ActorMaterializer()

  // Create the queue to publish to
  val queueName = "amqp-conn-it-test-simple-queue-" + System.currentTimeMillis()
  val queueDeclaration = QueueDeclaration(queueName)

  val connectionProvider = AmqpLocalConnectionProvider

  val queueSink: Sink[ByteString, Future[Done]] =
    AmqpSink.simple(
      AmqpWriteSettings(AmqpLocalConnectionProvider)
        .withRoutingKey(queueName)
        .withDeclaration(queueDeclaration)
    )

  val input = Vector("one", "two", "three", "four", "five")

  val writing: Future[Done] =
    Source(input)
      .map(s => ByteString(s))
      .runWith(queueSink)

}