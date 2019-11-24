# Scala event streaming app example

An example illustrating consuming AMQP messages and inserting a corresponding row into a
Postgresql database


## Setup
1. make sure you have docker-compose installed on your machine
2. run `docker-compose up -d` to run all containers in detached mode
3. Start the `scala-event-streaming` app
4. Using the rabbitmq console found at localhost:15627, publish a message to the queue named `amqp-conn-it-test-simple-queue-example`
    - an example message is 
    ```
   {
      "name": "Samus Aran",
      "email": "samus@federationforce.gov"
   } 
   ```
   
