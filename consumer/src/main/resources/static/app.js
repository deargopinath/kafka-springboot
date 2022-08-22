var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#trades").html("");
    var slidingWindow = setInterval(sendName, 3000);
    console.log(slidingWindow);
}

function connect() {
    var socket = new SockJS('/kafka-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/trades', function (trade) {
            showGreeting(JSON.parse(trade.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/query", {}, JSON.stringify({'name': 'trades'}));
}

function showGreeting(message) {
    var d = new Date();
    $("#lastUpdated").html("Last updated at " + d.toLocaleTimeString());
    $("#trades").empty();
    var records = message.split("\n");
    for(var k = 0; k < records.length; k++) {
        var record = records[k].replace(/\\|\"|\{|\}/g, '');
        $("#trades").append("<tr>");
        var columns = record.split(",");
        for (var e = 0; e < columns.length; e++) {
            $("#trades").append("<td>" + columns[e] + "</td>");
        }
        $("#trades").append("</tr>");
    }
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
});

