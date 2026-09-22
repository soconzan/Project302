// 채팅 리스트 필터링
const chatListFilter = document.querySelector('.chats-filter');
chatListFilter.addEventListener('click', function () {
    const chatFilters = document.querySelector('.chats-filter > ul');
    const isHidden = chatFilters.classList.contains('hide');
    if (isHidden) {
        chatFilters.classList.remove('hide');
    } else {
        chatFilters.classList.add('hide');
    }
});

{
    // 채팅방 클릭
    var links = document.querySelectorAll('.chat-link');

    links.forEach(function (link) {
        link.addEventListener("click", function () {
            removeAllClickedClass()
            link.classList.add("chat-clicked")
        });
    });

    function removeAllClickedClass() {
        links.forEach(function (link) {
            link.classList.remove("chat-clicked");
        });
    }
}