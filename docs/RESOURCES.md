# Resources

_Stages 1 and 8 do not require any additional knowledge._

## Stage 2 - Single chat

To complete this stage, know how to create tags with JavaScript and insert them on a page. Here is the code snippet that
shows you how to create a tag using JavaScript and add it to an already created container:

```javascript
const container = document.getElementById("container")

// create new div tag
const tag = document.createElement("div")
// add text to the tag
tag.textContent = "some text"
// add class "test" to the div
tag.classList.add("test")
// insert created tag at the end of the existing container
container.appendChild(tag)
```

Here is a [JavaScript tutorial](https://www.javascripttutorial.net/javascript-dom/javascript-createelement/) if you want
to learn more about this feature.

Furthermore, if the container is scrollable and was full before appending a new element to it, the new element will not
be visible. To see it, you will need to scroll. We can implement auto-scrolling with JS. Here is a simple code snippet
that shows how to scroll to an element with a "smooth" effect.

```javascript
tag.scrollIntoView({"behavior": "smooth"})
```

You can find more
details [JavaScript tutorial](https://www.javascripttutorial.net/javascript-dom/javascript-scrollintoview/) on
scrollIntoView.

Please don't hesitate to experiment with these features.

## Stage 3 - Group chat

WebSocket is a bidirectional, persistent connection between a web browser and a server. Once a WebSocket connection is
established, the connection stays open until the server or the client decides to close the connection. Cases of
WebSockets: chat apps, multiplayer games, collaborative apps, social feeds, location apps, etc. Take a look video [What
are WebSockets?](https://www.youtube.com/watch?v=i5OVcTdt_OU) by Tech Primers that contains more details.

To start working with the WebSocket in Spring Boot, add the following dependency to the build.gradle file:

```groovy
dependencies {
// ...
    implementation 'org.springframework.boot:spring-boot-starter-websocket'
// ...
}
```

To work with WebSocket, add these two imports before the closing tag `</body>` (if you already have JS import
statements, place the following imports before them):do

```html
// ...
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.0/sockjs.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
// ...
</body>
```

You can refer to the following articles to get a better idea:

- [Using WebSocket to build an interactive web application](https://spring.io/guides/gs/messaging-stomp-websocket/) by
Spring;
- [Create a simple Chat application with Spring Boot and Websocket](https://o7planning.org/10719/create-a-simple-chat-application-with-spring-boot-and-websocket)
by o7planning;
- [Getting Started with Spring WebSockets in Java](https://www.section.io/engineering-education/getting-started-with-spring-websockets/)
by John Amiscaray.

Additional tutorials that can help you:

- [Spring Boot WebSocket: Chat Example on YouTube](https://www.youtube.com/watch?v=-ao3pX-UhQc) by Java master
- [Building a chat application with Spring Boot and WebSocket](https://www.callicoder.com/spring-boot-websocket-chat-example/)
by Callicoder
- [Build Spring Boot Chat Application from scratch](https://www.pixeltrice.com/build-spring-boot-chat-application-from-scratch/)
by PixelTrice

## Stage 4 - Login and extended messages

Here is a link to the [Open Genus](https://iq.opengenus.org/intro-to-fetch-api/) tutorial that shows how to use the
JS `fetch` method to get data from an endpoint. The method `fetch` can also be used to send `POST` requests. It can help
you with stage and the following stages.

## Stage 5 - Online users

There are a couple of classes that start with `Session`. For example, `SessionConnectedEvent`, `SessionSubscribeEvent`,
and
`SessionDisconnectEvent`. If we create a class annotated with `@Component` and a method in the class annotated with
`@EventListener` that receives a session as an argument, this method will be called when an event is triggered. Here's
an
example:

```java
@Component
public class WebSocketEvents {

    @EventListener
    public void connect(SessionSubscribeEvent event) {
        System.out.println("Someone subscribed");
    }

    @EventListener
    public void disconnect(SessionDisconnectEvent event) {
        System.out.println("Someone disconnected");
    }
}
```

The code snippet above shows two methods that receive a session. The first method is called when someone subscribes, and
the second one is called when someone disconnects.

Closing the browser tab will trigger a disconnect event.

Take a look at the
article [Build a Chat Application using Spring Boot + WebSocket + RabbitMQ](https://www.javainuse.com/spring/boot-websocket-chat)
by Javainuse that contains information about WebSocket events and how to notify subscribed users when an event is
triggered.

## Stage 6 - Switch between chats

The
article [Remove all child elements of a DOM node in JavaScript](https://stackoverflow.com/questions/3955229/remove-all-child-elements-of-a-dom-node-in-javascript)
on Stack Overflow shows how to clear a container with elements using JS.

## Stage 7 - Private messages

This [Realtime Chatroom application - SpringBoot, Websocket, ReactJS](https://www.youtube.com/watch?v=o_IjEDAuo8Y&t=930s)
video by InvolveInInnovation shows how to send a message to a specific user.

