"use strict";

var usernamePage = document.querySelector("#username-page");
var chatPage = document.querySelector("#chat-page");
var usernameForm = document.querySelector("#usernameForm");
var messageForm = document.querySelector("#messageForm");
var messageInput = document.querySelector("#message");
var messageArea = document.querySelector("#messageArea");
var connectingElement = document.querySelector(".connecting");

var stompClient = null;
var username = null;
var password = null;

var colors = [
    "#2196F3",
    "#32c787",
    "#00BCD4",
    "#ff5652",
    "#ffc107",
    "#ff85af",
    "#FF9800",
    "#39bbb0",
    "#fcba03",
    "#fc0303",
    "#de5454",
    "#b9de54",
    "#54ded7",
    "#54ded7",
    "#1358d6",
    "#d611c6",
];

function connect() {

    var socket = new SockJS("/websocket");
   stompClient = Stomp.over(socket);
   stompClient.connect({

   }, onConnected, onError);

}

function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe("/topic/chat", onMessageReceived);

    // Tell your username to the server
    stompClient.send(
        "/chat/join", {},
        JSON.stringify({
            sender: username,
            type: "JOIN"
        })
    );


}

function onError(error) {
    let mes = document.getElementById("mes");
    mes.innerText = "Could not connect to WebSocket! Please check you credentials.";
}

function send(event) {
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: "CHAT",
        };

        stompClient.send("/chat/send", {}, JSON.stringify(chatMessage));
        messageInput.value = "";
    }
    event.preventDefault();
}

function deleteMessage(id) {
    if (id && stompClient) {
        var chatMessage = {
            id: id,
            type: "DELETE",
        };

        stompClient.send("/chat/send", {}, JSON.stringify(chatMessage));
    }
    event.preventDefault();
}

/**
 * Handles the received message and updates the chat interface accordingly.
 * param {Object} payload - The payload containing the message data.
 */
function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement("li");
    messageElement.setAttribute("id", message.id);

    if (message.type === "JOIN") {
        messageElement.classList.add("event-message");
        message.content = message.sender + " joined!";
    } else if (message.type === "LEAVE") {
        messageElement.classList.add("event-message");
        message.content = message.sender + " left!";
    } else if (message.type === "DELETE") {
        // Find the element by its ID
        var element = document.getElementById(message.id);
        if (element) {
            var paragraphElement = element.querySelector("p");
            var buttonElement = element.querySelector("button");

            for (var i = 0; i < paragraphElement.childNodes.length; i++) {
                var node = paragraphElement.childNodes[i];
                // Check if the node is a text node
                if (node.nodeType === Node.TEXT_NODE) {
                    // Replace the text content of the text node
                    node.textContent = "This message has been deleted";
                    buttonElement.remove();
                }
            }
        }
        return;
    } else {
        // Add the button to delete  message
        var button = document.createElement("button");
        button.classList.add("button");
        button.style.backgroundColor = 'red';
        button.innerHTML = "Delete";
        // Add click event listener to the button
        button.addEventListener('click', function() {
            deleteMessage(message.id)
        });
        messageElement.appendChild(button);

        // Show the button when mouseover
        messageElement.addEventListener("mouseover", function() {
            var button = this.querySelector('.button');
            if(button) {
              button.style.display = "inline-block";
            }
        });

        // Hide the button after mouseout
        messageElement.addEventListener("mouseout", function() {
            var button = this.querySelector('.button');
            if(button) {
               button.style.display = "none";
            }
        });

        messageElement.classList.add("chat-message");

        var avatarElement = document.createElement("i");

        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style["background-color"] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement("span");
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
        usernameElement.style["color"] = getAvatarColor(message.sender);
    }

    var textElement = document.createElement("p");

    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);
    if (message.sender === username) {
        // Add a class to float the message to the right
        messageElement.classList.add("own-message");
    }
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}


messageForm.addEventListener("submit", send, true);
connect();