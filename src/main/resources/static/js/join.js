let isIdChecked = false;
let isPasswordValid = false;
let isEmailAuthChecked = false;
let isNicknameChecked = false;

// 비밀번호 일치 확인
function passwordCheck() {
    const password = document.getElementById('password').value;
    const pwdCk = document.getElementById('pwdCk').value;
    const message = document.getElementById('pwdCkMessage');

    const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%\^\&*\)\(+=._-`~]).{8,}$/;

    if (!passwordRegex.test(password)) {
        message.textContent = '영문, 숫자, 특수문자를 조합하여 입력해주세요. (8-32자)';
        message.style.color = 'red';
        isPasswordValid = false;
    } else if (password !== pwdCk) {
        message.textContent = '비밀번호가 서로 일치하지 않습니다.';
        message.style.color = 'red';
        isPasswordValid = false;
    } else {
        message.textContent = '';
        isPasswordValid = true;
    }
}

// 이메일 도메인 설정
const domainListEl = document.querySelector('#domain-list')
const domainInputEl = document.querySelector('#domain-txt')
// select 옵션 변경 시
domainListEl.addEventListener('change', (event) => {
    // option에 있는 도메인 선택 시
    if(event.target.value !== "type") {
        // 선택한 도메인을 input에 입력하고 disabled
        domainInputEl.value = event.target.value
        domainInputEl.disabled = true
    } else { // 직접 입력 시
        // input 내용 초기화 & 입력 가능하도록 변경
        domainInputEl.value = ""
        domainInputEl.disabled = false
    }
})

function emailSend(){
    const emailLocalPart = document.getElementById('email').value.trim();
    const emailDomain = document.getElementById('domain-txt').value.trim();
    const fullEmail = emailLocalPart + "@" + emailDomain;
    document.getElementById('fullEmail').value=fullEmail;

    if (fullEmail) {
        fetch(`/join/mailSend?email=${fullEmail}`, {
            method: 'POST'
        })
            .then(response => response.json())
            .then(data => {
                if (data) {
                    alert("메일이 전송 됐습니다.");
                } else {
                    alert("뭐 이상함");
                }
            })
            .catch((error) => {
                console.error('Error:', error);
                alert("메일을 전송하는 중 문제가 발생했습니다. 다시 시도해 주세요.");
            });
    } else {
        alert("이메일을 입력해주세요.");
    }
}
function emailAuth(){
    const emailAuth = document.getElementById('emailAuthInput').value;
    const message = document.getElementById('emailMessage');
    const emailAuthInput = document.getElementById('emailAuthInput');


    if (emailAuth) {
        fetch(`/join/mailCheck?emailAuth=${emailAuth}`, {
            method: 'GET'
        })
            .then(response => response.json())
            .then(data => {
                if (data) {
                    // alert("맞음");
                    message.textContent = '인증번호가 일치합니다.';
                    message.style.color='blue';
                    emailAuthInput.disabled=true;
                    isEmailAuthChecked=true;
                } else {
                    // alert("인증번호가 일치하지 않습니다.");
                    message.textContent = '인증번호가 일치하지 않습니다.';
                    message.style.color = 'red';
                }
            })
            .catch((error) => {
                console.error('Error:', error);
                alert("문제가 발생했습니다. 다시 시도해 주세요.");
            });
    } else {
        alert("인증번호를 입력해주세요.");
    }


}

// '출생 연도' 셀렉트 박스 option 목록 동적 생성
const birthYearEl = document.querySelector('#birth-year')
// option 목록 생성 여부 확인
isYearOptionExisted = false;
birthYearEl.addEventListener('focus', function () {
    // year 목록 생성되지 않았을 때 (최초 클릭 시)
    if(!isYearOptionExisted) {
        isYearOptionExisted = true
        for(var i = 2024; i >= 1940; i--) {
            // option element 생성
            const YearOption = document.createElement('option')
            YearOption.setAttribute('value', i)
            YearOption.innerText = i
            // birthYearEl의 자식 요소로 추가
            this.appendChild(YearOption);
        }
    }
});
// Month
const birthMonthEl = document.querySelector('#birth-month')
// option 목록 생성 여부 확인
isMonthOptionExisted = false;
birthMonthEl.addEventListener('focus', function () {
    // month 목록 생성되지 않았을 때 (최초 클릭 시)
    if(!isMonthOptionExisted) {
        isMonthOptionExisted = true
        for(var i = 1; i <= 12; i++) {
            // option element 생성
            const MonthOption = document.createElement('option')
            MonthOption.setAttribute('value', i)
            MonthOption.innerText = i
            // birthMonthEl의 자식 요소로 추가
            this.appendChild(MonthOption);
        }
    }
});
const birthDayEl = document.querySelector('#birth-day')
// option 목록 생성 여부 확인
isDayOptionExisted = false;
birthDayEl.addEventListener('focus', function () {
    // day 목록 생성되지 않았을 때 (최초 클릭 시)
    if(!isDayOptionExisted) {
        isDayOptionExisted = true
        for(var i = 1; i <= 31; i++) {
            // option element 생성
            const DayOption = document.createElement('option')
            DayOption.setAttribute('value', i)
            DayOption.innerText = i
            // birthDayEl의 자식 요소로 추가
            this.appendChild(DayOption);
        }
    }
});
// 아이디 중복 체크
function checkUserId() {
    const localId = document.getElementById('localId').value;
    const message = document.getElementById('idCkeckMessage');

    if (localId.length > 3) {
        fetch(`/join/check-id?localId=${localId}`, {
            method: 'GET'
        })
            .then(response => response.json())
            .then(data => {
                if (data) {
                    // alert("사용 가능한 아이디입니다.");
                    message.textContent = '사용 가능한 아이디입니다.';
                    message.style.color = 'blue';
                    isIdChecked=true;
                } else {
                    // alert("이미 사용 중인 아이디입니다. 다른 아이디를 선택해 주세요.");
                    message.textContent = '이미 사용 중인 아이디입니다. 다른 아이디를 선택해 주세요.';
                    message.style.color='red';
                }
            })
            .catch((error) => {
                console.error('Error:', error);
                alert("ID를 확인하는 중 문제가 발생했습니다. 다시 시도해 주세요.");
            });
    } else {
        // alert("4자 이상 입력해주세요.");
        message.textContent='4자 이상 입력해주세요';
        message.style.color='red'
    }
}

function checkNickname() {
    const nickname = document.getElementById('nickname').value;
    const message = document.getElementById('nicknameCheckMessage');

    if (nickname) {
        fetch(`/join/check-nickname?nickname=${nickname}`, {
            method: 'GET'
        })
            .then(response => response.json())
            .then(data => {
                if (data) {
                    message.textContent = '사용 가능한 닉네임입니다.';
                    message.style.color = 'blue';
                    isNicknameChecked=true;
                } else {
                    message.textContent = '이미 사용 중인 닉네임입니다. 다른 닉네임을 선택해 주세요.';
                    message.style.color='red';
                }
            })
            .catch((error) => {
                console.error('Error:', error);
                alert("닉네임을 확인하는 중 문제가 발생했습니다. 다시 시도해 주세요.");
            });
    } else {
        alert("4자 이상 입력해주세요.");
    }
}
function ensureTwoDigits(num) {
    // 월, 일 포맷 변경
    return num < 10 ? '0' + num : num.toString();
}
function validateNumber(input) {
    input.value = input.value.replace(/[^0-9]/g, '');
}

function joinCheck(){

    const emailLocalPart = document.getElementById('email').value.trim();
    const emailDomain = document.getElementById('domain-txt').value.trim();
    const fullEmail = emailLocalPart + "@" + emailDomain;
    document.getElementById('fullEmail').value=fullEmail;

    // Concatenate phone number parts
    const phonePart1 = document.querySelector('[name="cel1"]').value.trim();
    const phonePart2 = document.querySelector('[name="cel2_1"]').value.trim();
    const phonePart3 = document.querySelector('[name="cel2_2"]').value.trim();
    const fullPhoneNumber = `${phonePart1}${phonePart2}${phonePart3}`;
    // Assuming there's an input in your form to hold the full phone number:
    document.getElementById('fullPhoneNumber').value = fullPhoneNumber;

    // Concatenate birth date parts
    const birthYear = document.querySelector('[name="birth-year"]').value;
    const birthMonth = document.querySelector('[name="birth-month"]').value;
    const birthDay = document.querySelector('[name="birth-day"]').value;
    const formattedMonth = ensureTwoDigits(parseInt(birthMonth));
    const formattedDay = ensureTwoDigits(parseInt(birthDay));

    // Combine year, month, and day into a full date string
    const fullBirthDate = `${birthYear}${formattedMonth}${formattedDay}`;

    // Assuming there's an input in your form to hold the full birth date:
    document.getElementById('fullBirthDate').value = fullBirthDate;



    const localId = document.getElementById('localId').value;
    const password = document.getElementById('password').value;
    const nickname = document.getElementById('nickname').value;
    const genderElement = document.querySelector('input[name="gender"]:checked');
    const gender = genderElement ? genderElement.value : '';
    

    // 필수 입력 항목 확인
    if (!localId || !password || !fullEmail || !fullPhoneNumber|| !nickname || !fullBirthDate|| !gender) {
        alert('모든 항목을 입력해주세요.');
        return false;
    }

    // 각 검사 통과 여부 확인
    if (!isIdChecked) {
        alert('아이디 중복 확인을 해주세요.');
        return false;
    }
    if (!isPasswordValid) {
        alert('비밀번호가 일치하지 않습니다.');
        return false;
    }
    if (!isEmailAuthChecked) {
        alert('이메일 인증을 해주세요.');
        return false;
    }
    if (!isNicknameChecked) {
        alert('닉네임 중복 확인을 해주세요.');
        return false;
    }

    // Compile userData
    const userData = {
        localId: document.getElementById('localId').value.trim(),
        password: document.getElementById('password').value.trim(),
        email: fullEmail,
        phoneNo: fullPhoneNumber,
        nickname: document.getElementById('nickname').value.trim(),
        birth: fullBirthDate,
        gender: document.querySelector('input[name="gender"]:checked').value
    };
    document.getElementById('joinForm').submit();
}