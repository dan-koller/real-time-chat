# App Configuration

If you want to use the app with RabbitMQ, you can use the following configuration to set up a local RabbitMQ server
using Docker. Note that you need to update the routes in the `*Controller` classes to point to the correct RabbitMQ
destination.

**RabbitMQ does not support private messaging with the way we have implemented it.**

## RabbitMQ Docker Setup

- Create a docker container with the following command:

```bash
docker run -d --hostname rmq --name rabbit-server -p 8080:15672 -p 61613:61613 rabbitmq:3-management
```

- Enable the STOMP plugin:

```bash
rabbitmq-plugins enable rabbitmq_stomp
```

- Restart the container:

```bash
docker restart rabbit-server
```

The RabbitMQ management console is now available at http://localhost:8080.

## MongoDB Docker Setup

- Download the MongoDB Community Server Docker image:

```bash
docker pull mongo
```

_If you are on ARM, you need to use this image:_

```bash
docker pull arm64v8/mongo
```

- Create a docker container with the following command:

```bash
docker run -d --name mongo-server -p 27017:27017 mongo
```

_If you are on ARM, you need to use this command:_

```bash
docker run -d --name mongo-server -p 27017:27017 arm64v8/mongo
```

- Connect to the container:

```bash
docker exec -it mongo-server mongosh
```

### Using MongoDB

If you want to use MongoDB as your database, you need to update several files in this project.

1. Add the dependency to the `build.gradle` file:

```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-mongodb'
```

2. Add the connection string to the `application.properties` file:

```properties
spring.data.mongodb.uri=mongodb://<HOSTNAME>:27017/chat
```

3. Update the `ChatMessage.class` file to use the `@Document` and `@MongoId` annotations:

```java
package com.example.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "messages")
public class ChatMessage {
    @MongoId
    private String id;
    private String sender;
    private String message;
    private String date;
    private String sendTo;
    private MessageType messageType;
}
```

4. Update the `ChatMessageRepository.class` file to use the `MongoRepository` interface:

```java
package com.example.chat.repository;

import com.example.chat.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findBySenderAndSendToOrSenderAndSendToOrderByDateAsc(String sender, String sendTo, String sendTo2, String sender2);

    List<ChatMessage> findByMessageTypeEqualsOrderByDateAsc(MessageType messageType);
}
```

**Make sure to have your MongoDB server running before starting the app.**

## MySQL Docker Setup

- Download the MySQL Docker image:

```bash
docker pull mysql
```

- Create a docker container with the following command:

```bash
docker run --name mysql -e MYSQL_ROOT_PASSWORD=<YOUR_PASSWORD> -p 3306:3306 -d mysql
```

### Using MySQL

The app is already configured to use MySQL as the database. You just need to create a `.env` file and define the
following variables:

```bash
cd real-time-chat
cp /src/main/resources/.env.example /src/main/resources/.env
```

```properties
MYSQL_DATABASE_URL=jdbc:mysql://localhost:3306/chat
MYSQL_DATABASE_USERNAME=<YOUR_USERNAME>
MYSQL_DATABASE_PASSWORD=<YOUR_PASSWORD>
```
