var stompClient = null;

$(document).ready(function () {
    disconnect();
    connect();
});

// 소켓 연결
function connect() {
    var socket = new SockJS("/ws-stomp");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {

        fetch('/api/socket')
            .then(function (response) {
                if (!response.ok) {
                    throw new Error('No find WebSocket');
                }
                return response.json();
            })
            .then(function (chatIds) {
                // console.log(chatIds);
                chatIds.forEach(function (chatId) {
                    stompClient.subscribe('/chatroom/' + chatId, function (messages) {
                        // notification
                        showChatBadge()
                    });
                });
            });
    });
}

// 소켓 연결 해제
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
}

function showChatBadge() {
    $('#aside-badge').removeClass('hide');
}
