const inputMsg = document.getElementById("input-msg");
const sendMsgBtn = document.getElementById("send-msg-btn");
const messages = document.getElementById("messages");
const sendUsernameBtn = document.getElementById("send-username-btn")
const inputUsername = document.getElementById("input-username")
const loginPage = document.getElementById("login-page")
const chatPage = document.getElementById("chat-page")
const users = document.getElementById("users")
const chatWith = document.getElementById("chat-with")
const publicChatBtn = document.getElementById("public-chat-btn")

let sockJS;
let stompClient;
let username;

// If the user presses the enter key, append the message to the chat
inputMsg.addEventListener("keyup", function (event) {
    if (event.key === "Enter") {
        sendMessage();
    }
});

// If the user clicks the send button, append the message to the chat
sendMsgBtn.addEventListener("click", function () {
    sendMessage();
});

function appendMsg(sender, message, date) {
    const divMessage = document.createElement("div");
    const divSender = document.createElement("div");
    const divText = document.createElement("div");
    const divDate = document.createElement("div");

    divMessage.classList.add("message-container");
    divSender.classList.add("sender");
    divText.classList.add("message");
    divDate.classList.add("date");

    divMessage.appendChild(divSender);
    divMessage.appendChild(divText);
    divMessage.appendChild(divDate);

    divSender.textContent = sender;
    divText.textContent = message;
    divDate.textContent = date;

    // Apply the system font to all the elements
    divSender.style.fontFamily = "system-ui";
    divDate.style.fontFamily = "system-ui";

    messages.appendChild(divMessage);

    divMessage.scrollIntoView({ behavior: "smooth" });
}

function onPublicMessageReceived(payload) {
    const response = JSON.parse(payload.body);

    if (chatWith.textContent === "Public chat") {
        appendMsg(response.sender, response.message, response.date)
    } else {
        const counter = publicChatBtn.getElementsByClassName("new-message-counter")[0]
        counter.textContent = 1 + parseInt(counter.textContent)
        counter.style.visibility = "visible"
    }
}

function onPrivateMessageReceived(payload) {
    const response = JSON.parse(payload.body)
    const messages = response.messages
    const messageType = response.messageType

    console.warn("Message received: " + messages[0].sender + " " + messages[0].sendTo)

    if (chatWith.textContent === messages[0].sender || chatWith.textContent === messages[0].sendTo) {
        for (let i in messages) {
            appendMsg(messages[i].sender, messages[i].message, messages[i].date)
        }
    } else {
        const userContainers = document.getElementsByClassName("user-container")
        for (let i in userContainers) {
            if (userContainers[i].getElementsByClassName("user")[0].textContent === messages[0].sender) {
                const counter = userContainers[i].getElementsByClassName("new-message-counter")[0]
                counter.style.visibility = "visible"
                counter.textContent = parseInt(counter.textContent) + messages.length
                break
            }
        }
    }

    if (messageType === "PRIVATE") {
        moveUserToTop(messages[0].sender)
    }
}

function addUserToUserList(user) {
    const divUserContainer = document.createElement("div")
    const divUser = document.createElement("div")
    const divNewMessageCounter = document.createElement("div")

    divUserContainer.classList.add("user-container")
    divUser.classList.add("user")
    divNewMessageCounter.classList.add("new-message-counter")

    divUser.textContent = user
    divNewMessageCounter.textContent = "0"
    divNewMessageCounter.style.visibility = "hidden"

    divUserContainer.appendChild(divUser)
    divUserContainer.appendChild(divNewMessageCounter)

    divUserContainer.onclick = () => {
        chatWith.textContent = user
        messages.replaceChildren()

        const chatMessage = {
            "sender": this.username,
            "sendTo": user,
            "messageType": "GET_ALL"
        }

        stompClient.send("/app/chat.send.private", {}, JSON.stringify(chatMessage))

        divNewMessageCounter.textContent = "0"
        divNewMessageCounter.style.visibility = "hidden"
    }

    users.appendChild(divUserContainer)
}

function removeUserFromUserList(username) {
    const usersContainers = users.getElementsByClassName("user-container")
    for (const i in usersContainers) {
        if (usersContainers[i].getElementsByClassName("user")[0].textContent === username) {
            usersContainers[i].remove()
            break
        }
    }
}

function onUserEventReceived(payload) {
    const response = JSON.parse(payload.body);

    if (response.userEventType === "JOINED" && response.username !== this.username) {
        addUserToUserList(response.username)
    } else if (response.userEventType === "LEFT") {
        removeUserFromUserList(response.username)
    }
}

function onConnect() {
    let username = getUsername()
    const user = {
        username: username,
    }

    stompClient.subscribe("/chat/public", onPublicMessageReceived)
    stompClient.subscribe("/user/" + username + "/private", onPrivateMessageReceived)
    stompClient.subscribe("/chat/user-event", onUserEventReceived)
    stompClient.send("/app/chat.register", {}, JSON.stringify(user))
}

function onError() {
    console.log("Something went wrong");
}

function sendMessage() {
    if (inputMsg.value) {
        if (chatWith.textContent === "Public chat") {
            stompClient.send("/app/chat.send", {}, JSON.stringify({
                "sender": this.username,
                "message": inputMsg.value,
                "date": getCurrentDate(),
            }))
        } else {
            stompClient.send("/app/chat.send.private", {}, JSON.stringify({
                "sender": this.username,
                "message": inputMsg.value,
                "date": getCurrentDate(),
                "sendTo": chatWith.textContent,
                "messageType": "PRIVATE"
            }))
            moveUserToTop(chatWith.textContent)
        }
        inputMsg.value = "";
    }
}

function loadPublicMessages() {
    window.fetch("/public-messages").then((response) =>
        response.json().then((data) => {
            for (let i in data) {
                appendMsg(data[i].sender, data[i].message, data[i].date);
            }
        })
    );
}

function moveUserToTop(username) {
    const userContainers = users.getElementsByClassName("user-container")

    if (userContainers.length >= 2) {
        for (let i = 0; i < userContainers.length; i++) {
            if (userContainers[i].getElementsByClassName("user")[0].textContent === username) {
                users.prepend(userContainers[i])
            }
        }
    }
}

sendUsernameBtn.onclick = () => {
    this.username = inputUsername.value || "Anonymous";
    generateAvatar();

    window.fetch("/users")
        .then(response => response.json()
            .then(data => {
                for (let i in data) {
                    if (username === data[i]) {
                        document.getElementById("login-error").style.visibility = "visible"
                        return
                    }
                }

                loginPage.style.display = "none" // hide login page
//                loginPage.classList.add("hidden")
                chatPage.classList.remove("hidden")

                for (let i in data) {
                    addUserToUserList(data[i])
                }

                loadPublicMessages()

                sockJS = new SockJS("/ws")
                stompClient = Stomp.over(sockJS)
                stompClient.connect({}, onConnect, onError);

                sendMsgBtn.onclick = () => {
                    sendMessage()
                    inputMsg.value = ""
                }
            }))
}

publicChatBtn.onclick = () => {
    messages.replaceChildren()
    loadPublicMessages()
    chatWith.textContent = "Public chat"
}

// Get the username
function getUsername() {
    return this.username;
}

// Get the current date and time in the format dd.mm.yyyy hh:mm
function getCurrentDate() {
    const date = new Date();
    const day = date.getDate();
    const month = date.getMonth() + 1;
    const year = date.getFullYear();
    const hours = date.getHours() < 10 ? `0${date.getHours()}` : date.getHours();
    const minutes = date.getMinutes() < 10 ? `0${date.getMinutes()}` : date.getMinutes();
    const seconds = date.getSeconds() < 10 ? `0${date.getSeconds()}` : date.getSeconds();
    return `${day}.${month}.${year} ${hours}:${minutes}`;
}

// Function to generate a colorful avatar based on the user's name
function generateAvatar() {
    name = this.username || "Anonymous";
    let initials = name
        .split(" ")
        .map(function (str) {
            return str ? str[0].toUpperCase() : "";
        })
        .join("");
    let canvas = document.getElementById("canvas");
    let radius = 20;
    let margin = 5;
    canvas.width = radius * 2 + margin * 2;
    canvas.height = radius * 2 + margin * 2;

    // Pick a random color
    const colors = [
        "#f44336",
        "#e91e63",
        "#9c27b0",
        "#673ab7",
        "#3f51b5",
        "#2196f3",
        "#03a9f4",
        "#00bcd4",
        "#009688",
        "#4caf50",
        "#8bc34a",
        "#cddc39",
        "#ffeb3b",
        "#ffc107",
        "#ff9800",
        "#ff5722",
        "#795548",
        "#9e9e9e",
        "#607d8b",
    ];
    const color = colors[Math.floor(Math.random() * colors.length)];

    // Get the drawing context
    let ctx = canvas.getContext("2d");
    ctx.beginPath();
    ctx.arc(radius + margin, radius + margin, radius, 0, 2 * Math.PI, false);
    ctx.closePath();
    ctx.fillStyle = color;
    ctx.fill();
    ctx.fillStyle = "white";
    ctx.font = "bold 20px system-ui";
    ctx.textAlign = "center";
    ctx.fillText(initials, radius + 5, (radius * 4) / 3 + margin);

    console.log(canvas.toDataURL());

    return canvas.toDataURL();
};
