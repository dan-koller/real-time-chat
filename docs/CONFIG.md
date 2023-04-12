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
