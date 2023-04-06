# Frequently Asked Questions

## Why does `convertAndSendToUser` not work properly with RabbitMQ?

`convertAndSendToUser` is a method provided by Spring Messaging to send a message to a specific user in a Spring
application. When using this method, Spring creates a unique destination for each user by appending a session ID to the
destination. For example, if the user with a session ID of "abc" subscribes to `/user/queue/private`, Spring will create a
unique destination such as `/user/abc/queue/private` for this user.

However, when using RabbitMQ as a message broker, the convertAndSendToUser method is not compatible with the default
RabbitMQ message routing configuration. RabbitMQ's default configuration routes messages based on a destination pattern,
which is defined as a string that includes routing key and exchange name, and does not include the user's session ID.

This means that when using convertAndSendToUser with RabbitMQ, the message will be sent to the default destination
pattern of the exchange, and not the unique destination for the user. Therefore, the user will not receive the message
intended for them.

To solve this problem, you need to customize the RabbitMQ message routing configuration to use the unique destination
pattern for each user. This can be achieved by creating a custom RabbitMQ message broker configuration, and using it in
your Spring application instead of the default configuration.

Alternatively, you can use the convertAndSend method instead of convertAndSendToUser, and include the user's session ID
in the message payload or headers. Then, on the client side, you can filter and route the messages based on the session
ID. However, this approach requires more effort and can be less efficient compared to the custom message routing
configuration.