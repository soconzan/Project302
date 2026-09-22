console.log("저용");

var latitude = [[${product.latitude}]];
var longitude = [[${product.longitude}]];

var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
        center: new kakao.maps.LatLng(latitude, longitude), // 지도의 중심좌표
        level: 3 // 지도의 확대 레벨
    };

var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
// var map = new daum.maps.Map(mapContainer, mapOption);

// 지도를 클릭한 위치에 표출할 마커입니다
var marker = new kakao.maps.Marker({
    // 지도 중심좌표에 마커를 생성합니다
    position: map.getCenter()
});

// 지도에 마커를 표시합니다
marker.setMap(map);

// 지도에 클릭 이벤트를 등록합니다
// 지도를 클릭하면 마지막 파라미터로 넘어온 함수를 호출합니다
kakao.maps.event.addListener(map, 'click', function (mouseEvent) {

    // 클릭한 위도, 경도 정보를 가져옵니다
    var latlng = mouseEvent.latLng;

    // 마커 위치를 클릭한 위치로 옮깁니다
    marker.setPosition(latlng);

    // input 요소 선택
    var inputLat = document.getElementById("latitude");
    inputLat.value = latlng.getLat();
    var inputLng = document.getElementById("longitude");
    inputLng.value = latlng.getLng();

    console.log("lat : " + inputLat.value + ", lng : " + inputLng.value);

});

// 현재 날짜 이후로 마감일 설정
const clos = document.getElementById('scheduleDate');
const date = new Date(new Date().getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, -5);
clos.value = date;
clos.setAttribute("min", date);

function setMinValue() {
    if (clos.value < date) {
        alert("현재 시간보다 이전의 날짜는 설정할 수 없습니다.");
        clos.value = date;
    }
}

// 창 닫기
$('#chatroom-modal-close').on('click', function (event) {
    $('#chatroom-modal').addClass('hide');
});