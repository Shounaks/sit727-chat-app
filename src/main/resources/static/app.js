let ws;

function connect() {
    const username = document.getElementById("username").value;
    if (!username) {
        alert("Please enter a username");
        return;
    }

    ws = new WebSocket("ws://" + window.location.host +"/chat");

    ws.onopen = function () {
        document.getElementById("message").disabled = false;
        document.getElementById("sendBtn").disabled = false;
        document.getElementById("username").disabled = true;
    };

    ws.onmessage = function (event) {
        const messagesDiv = document.getElementById("messages");
        messagesDiv.innerHTML += "<p>" + event.data + "</p>";
        messagesDiv.scrollTop = messagesDiv.scrollHeight;
    };

    ws.onclose = function () {
        document.getElementById("message").disabled = true;
        document.getElementById("sendBtn").disabled = true;
        document.getElementById("username").disabled = false;
    };
}

function sendMessage() {
    const username = document.getElementById("username").value;
    const message = document.getElementById("message").value;
    if (message && ws) {
        ws.send(username + ":" + message);
        document.getElementById("message").value = "";
    }
}