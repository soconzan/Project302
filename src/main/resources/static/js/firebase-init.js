// Firebase 초기화
var firebaseConfig = {
    apiKey: "AIzaSyDZmI-48TFGkVOM8rz9bWPB2t-esjuIKi0",
    authDomain: "project302-1daf6.firebaseapp.com",
    projectId: "project302-1daf6",
    storageBucket: "project302-1daf6.appspot.com",
    messagingSenderId: "289908898161",
    appId: "1:289908898161:web:4250ddb9637465bc799ed0",
    measurementId: "G-MN2S5B4M5P"
};
firebase.initializeApp(firebaseConfig);

const messaging = firebase.messaging();

document.getElementById('request-permission-btn').addEventListener('click', () => {
    messaging.requestPermission()
        .then(() => {
            console.log('Notification permission granted.');
            return messaging.getToken({vapidKey: 'BD7LvVrwsTdZV1LU7HHSl9S-AVK6lwZwutMPcbVpvaBNUYqEt5qT6tI9-PTEQUTPHPNqnLPlTDwz3FL_COQ-LFQ'});
        })
        .then((token) => {
            console.log('FCM Token:', token);
            // 서버에 토큰 전달
        })
        .catch((err) => {
            console.log('Unable to get permission to notify.', err);
        });
});

messaging.onMessage((payload) => {
    console.log('Message received. ', payload);
    // 알림 표시 코드 추가
});
