// Import the functions you need from the SDKs you need
// import {initializeApp} from "firebase/app";
// import {getAnalytics} from "firebase/analytics";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Import the functions you need from the SDKs you need
import { initializeApp } from "https://www.gstatic.com/firebasejs/10.12.1/firebase-app.js";
import { getAnalytics } from "https://www.gstatic.com/firebasejs/10.12.1/firebase-analytics.js";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
    apiKey: "AIzaSyDZmI-48TFGkVOM8rz9bWPB2t-esjuIKi0",
    authDomain: "project302-1daf6.firebaseapp.com",
    projectId: "project302-1daf6",
    storageBucket: "project302-1daf6.appspot.com",
    messagingSenderId: "289908898161",
    appId: "1:289908898161:web:4250ddb9637465bc799ed0",
    measurementId: "G-MN2S5B4M5P"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);