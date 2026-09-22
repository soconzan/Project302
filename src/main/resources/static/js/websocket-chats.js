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
                chatIds.forEach(function (chatId) {
                    stompClient.subscribe('/chatroom/' + chatId, function (messages) {
                        // udpate chatlist
                        reloadChatList();
                        // update chatroom
                        showMessages(JSON.parse(messages.body));
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


function showMessages(messages) {

    // 현재 접속중인 채팅방이라면
    if (messages[0].chatId === chatId) {
        messages.forEach(function (message) {
            var html = '';
            var createDate = new Date(message.creatDate);
            var formattedTime = ('0' + createDate.getHours()).slice(-2) + ':' + ('0' + createDate.getMinutes()).slice(-2);

            if (message.userId == null) {
                html = '<div class="msg-notice" th:text="' + message.constructor + '"></div>'
            } else if (message.userId == userId) {
                html += '<div class="outbox">' +
                    '<div class="outbox-msg">';
                if (message.content == null) {
                    html += '<a href="' + message.chatFileLink + '" target="_blank" class="msg-file">' +
                        '<img src="' + message.chatFileLink + '">' +
                        '</a>';
                } else {
                    html += '<div class="outbox-content">' + message.content + '</div>';
                }
                html += '<div class="outbox-info">' +
                    '<p class="msg-read">' + message.readNot + '</p>' +
                    '<p class="msg-time">' + formattedTime + '</p>' +
                    '</div>' +
                    '</div>' +
                    '</div>';
            } else {
                html += '<div class="inbox">' +
                    '<div class="msg-profile">' +
                    '<img src="' + message.userFileLink + '">' +
                    '</div>' +
                    '<div class="inbox-msg-list">' +
                    '<p class="msg-nickname">' + message.nickname + '</p>' +
                    '<div class="inbox-msg">';
                if (message.content == null) {
                    html += '<a href="' + message.chatFileLink + '" target="_blank" class="msg-file">' +
                        '<img src="' + message.chatFileLink + '">' +
                        '</a>';
                } else {
                    html += '<div class="inbox-content">' + message.content + '</div>';
                }
                html += '<div class="inbox-info">' +
                    '<p class="msg-read">' + message.readNot + '</p>' +
                    '<p class="msg-time">' + formattedTime + '</p>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>';
            }
            $("#chatroom-messages").prepend(html);
        });
    }
}


function reloadChatList() {
    // 채팅 리스트 업데이트
    var url = "/chats/list";
    $.get(url, function (data) {
        $('.chat-list').html(data);
    });
}