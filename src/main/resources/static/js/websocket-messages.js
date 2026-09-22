var userId = [[${user.userId}]];
var chatId = [[${chatId}]];

$(document).ready(function () {
    $("#send").click(function () {
        sendMessage();
    });

    $('#send-message').on('keydown', function (event) {
        if (event.keyCode === 13) {
            event.preventDefault();
            sendMessage();
        }
    });

});

function sendMessage() {
    var content = $("#send-message").val();
    var files = $("#send-files")[0].files;

    // 파일 있으면 서버로 전송
    if (files.length > 0) {
        var formData = new FormData();
        for (var i = 0; i < files.length; i++) {
            formData.append('files', files[i]);
        }

        fetch('/chats/' + chatId + '/file', {
            method: 'POST',
            body: formData
        })
            .then(response => response.json()) // 서버에서 반환한 JSON을 읽음
            .then(data => {
                stompClient.send("/send/file/" + chatId, {}, JSON.stringify(data));
                $("#image-preview").addClass('hide');
                $("#send-files").val('');
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    // 메시지 있으면 전송
    if (content !== "") {
        var message = {
            'content': content,
            'userId': userId,
            'chatId': chatId
        };
        stompClient.send("/send/" + chatId, {}, JSON.stringify(message));
        $("#send-message").val('');
    }
}

function showMessages(messages) {

    // 현재 접속중인 채팅방이라면
    if (messages[0].chatId === chatId) {
        messages.forEach(function (message) {
            var html = '';
            var createDate = new Date(message.creatDate);
            var formattedTime = ('0' + createDate.getHours()).slice(-2) + ':' + ('0' + createDate.getMinutes()).slice(-2);

            if (message.userId == userId) {
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