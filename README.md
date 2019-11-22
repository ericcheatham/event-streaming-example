# Scala event streaming app example

Basic example illustraing consuming AMQP messages and inserting a corresponding row into a
Postgresql database


## Setup
1. make sure you have docker-compose installed on your machine
2. run `docker-compose up -d` to run all containers in detached mode
3. Set up database using the following:
    ```
   CREATE TABLE customers (
        id  uuid PRIMARY KEY,
        name varchar(100),
        email varchar(100)
    );
   ```
4. Start the `scala-event-streaming` app
5. Using the rabbitmq console found at localhost:15627, publish a message to the queue named `amqp-conn-it-test-simple-queue-example`
    - an example message is 
    ```
   {
      "id": "7de35905-bee3-496e-8737-14b51a67d52d",
      "name": "Samus Aran",
      "email": "samus@federationforce.gov"
   } 
   ```