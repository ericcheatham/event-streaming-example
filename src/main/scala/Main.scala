import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.amqp.scaladsl.AmqpSource
import akka.stream.alpakka.amqp.{AmqpLocalConnectionProvider, NamedQueueSourceSettings, QueueDeclaration, ReadResult}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.{Sink, Source}
import models.CustomerCreated
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization.read
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global

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
      .alsoTo(Sink.foreach { x ⇒ println(s"Received message: $x") })
      .runWith(
        Slick.sink(customer ⇒ sqlu"INSERT INTO customers (id, name, email) VALUES ${customer.id}, ${customer.name}, ${customer.email} ")
      )



//email  result.map({ customers ⇒
//    Source(customers)
//      .runWith(
//        Slick.sink(customer ⇒ sqlu"INSERT INTO customers (id, name, email) VALUES ${customer.id}, ${customer.name}, ${customer.email} ")
//      )
//  })

}