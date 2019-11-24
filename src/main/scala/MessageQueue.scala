import akka.NotUsed
import akka.stream.alpakka.amqp.scaladsl.AmqpSource
import akka.stream.alpakka.amqp.{AmqpConnectionProvider, Declaration, NamedQueueSourceSettings, ReadResult}
import akka.stream.scaladsl.Source

object MessageQueue {
  def createQueue(connectionProvider: AmqpConnectionProvider,
                  queueName: String,
                  queueDeclaration: Declaration,
                  bufferSize: Int = 10): Source[ReadResult, NotUsed] =
    AmqpSource.atMostOnceSource(
      NamedQueueSourceSettings(connectionProvider, queueName)
        .withDeclaration(queueDeclaration)
        .withAckRequired(true),
      bufferSize = bufferSize
    )
}