document.addEventListener('DOMContentLoaded', function () {
    var chatId = /*[[${chatId}]]*/ null;

    if (chatId != null) {
        // $('#chat-right').html("");
        var url = `/chats/${chatId}`;
        $.get(url, function (data) {
            $('#chat-right').html(data);
        });
    }

    $('.chat-link').click(function (event) {
        event.preventDefault();
        var url = $(this).attr('href');
        $.get(url, function (data) {
            $('#chat-right').html(data);
        });
    });
});