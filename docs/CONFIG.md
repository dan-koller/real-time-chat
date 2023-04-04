# App Configuration

## Docker setup

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


