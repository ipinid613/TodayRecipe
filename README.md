# TodayRecipe(서버)
### 목차
- [개발 배경](#개발-배경)
- [개발 과정](#개발-과정)
  - [개발 기간](#1-개발-기간)
  - [사용 언어](#2-사용-언어)
  - [기능 구현 목표](#3-기능-구현-목표)
  - [와이어프레임 설계](#4-와이어프레임-설계)
  - [API 설계](#5-API-설계)
- [개발 결과](#개발-결과)
  - [구현한 기능](#1구현한-기능)
  - [실제 서비스 모습](#2-실제-서비스-모습)
  - [피드백](#3-회고-및-피드백)
---
### 개발 배경
- 오픈된 공간에서 유저 서로가 맛있는 음식의 레시피를 공유하는 공간입니다!
- 개발 구상 단계에서 저희 팀 4명은 배가 고팠습니다. 맛있는 음식이 먹고싶다는 이야기를 하는 도중 떠오른 아이디어로 개발하게 되었습니다.
- 이번 프로젝트는 각자가 주특기`개인의 선호에 따라 백엔드(Java Spring), 프론트엔드(React) 선택` 를 공부하고 진행하는 첫 협업이었습니다.
---
### 개발 과정

#### 1. 개발 기간
- `2021년 07월 09일(금) ~ 2021년 07월 15일(목) / 총 7일`
- `설계 1일 / 개발 6일`

#### 2. 사용 언어
- **Languages** : Java(Backend), React(Frontend)
- **Framework** : Spring
- **DB** : MySQL, H2(테스트용)

#### 3. 프로젝트 목표
1. 서로 다른 개발환경에서의 연동(CORS)
2. 회원가입 & Spring에서 JWT 방식의 로그인
3. 게시판 구현(CRUD 구현, 이미지 업로드)
4. 댓글 작성(CRUD 개념 적용)
5. 검색
6. 비밀번호 변경

#### 4. 와이어프레임 설계(Figma 활용)
[ㅇ](https://www.figma.com/file/oZF7U6mjOvXaIq3nfeeKKf/%ED%95%AD%ED%95%B499-%EB%AF%B8%EB%8B%88%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-24%EC%A1%B0?node-id=0%3A1)

1. 회원가입
![회원가입1](https://user-images.githubusercontent.com/85334989/128336199-996a5d6c-6e70-4e7a-8848-2b622d8f52f4.png width="700" height="500")
![회원가입2](https://user-images.githubusercontent.com/85334989/128336294-725691be-8ff4-40e8-a505-3a63f1ddd1fb.png width="700" height="500")


2. 로그인
![로그인](https://user-images.githubusercontent.com/85334989/128336344-2b783a07-e22f-48f8-823a-488808dbceff.png width="700" height="500")

3. 메인페이지
![메인페이지](https://user-images.githubusercontent.com/85334989/128336417-12e97821-06d1-4a1d-8a6c-2ab194008070.png width="700" height="500")

4. 상세 페이지
![상세 페이지](https://user-images.githubusercontent.com/85334989/128336485-dccccaa6-038a-48ac-8755-161eec7a2638.png width="700" height="1000")

5. 게시글 작성&수정 페이지
![게시글 작성 페이지](https://user-images.githubusercontent.com/85334989/128336550-ab747d6a-5244-4027-aa32-aa8a39805f4e.png width="700" height="500")

#### 5. API 설계

| 페이지 | 기능 | API URL | Method |
|:----------|:----------:|:----------:|:----------:|
| letin.html | 회원가입 | /sign_up/save | POST |
| letin.html | 중복가입 체크 - 아이디 | /sign_up/checkDup | POST |
| letin.html | 중복가입 체크 - 닉네임 | /sign_up/nik_checkDup | POST |
| login.html | 로그인 | / | GET, POST |
| index.html | 메인페이지 / 게시글 전체조회 | /home | - |
| write.html | 게시글 작성 | /contents | POST |
| update.html | 게시글 수정 | /update | POST |
| write.html | 게시글 삭제 | /delete | POST | 

---
### 개발 결과

#### 1.구현한 기능
처음 목표했던 7가지 기능 중, 3가지(`회원가입, 로그인, CRUD 게시판`)를 구현하였습니다. 나머지 4가지 기능은 끝까지 시도하다가 시간적 제약으로 인해 제외하고 배포를 하였습니다.</br></br>
대신, 저희 팀이 구현할 수 있던 위 3가지 기능에 추가적인 기능(후술)을 더하고, 사용자가 보기에 예쁜 웹을 만들기 위해 노력했습니다.</br></br>
- 정규식을 활용한 회원가입 시 입력값 검증 기능
```javascript
// {# 아이디, 비밀번호 정규식!! #}

function is_username(asValue) {
    //{# 괄호 ( )안의 요소는 필수 포함 요소임. a-zA-Z 소문자 a-z, 대문자 A-Z 포함! 대괄호는 선택포함을 의미함. 숫자 0-9사용가능!. 2-10자여야 한다.#}
    var regExp = /^(?=.*[a-zA-Z])[-a-zA-Z0-9_.]{2,10}$/;
    return regExp.test(asValue); // {# .test 메서드를 통해 boolean 값으로 return 가능 #}
}

function is_nickname(asValue) {
    var regExp = /^[가-힣ㄱ-ㅎa-zA-Z0-9._-]{2,}$/;
    return regExp.test(asValue);
}

function is_password(asValue) {
    // {# *\d = 숫자 무조건 포함해라#}
    var regExp = /^(?=.*\d)(?=.*[a-zA-Z])[0-9a-zA-Z!@#$%^&*]{8,20}$/;
    return regExp.test(asValue);
```

- 회원가입 시 아이디 중복확인 기능
```javascript
// {# 아이디 중복확인 #}

function check_dup() {
    let username = $("#input-username").val()
    if (username == "") {
        $("#help-id").text("아이디를 입력해주세요.").removeClass("is-safe").addClass("is-danger")
        $("#input-username").focus()
        return;
    }
    if (!is_username(username)) {
        $("#help-id").text("아이디의 형식을 확인해주세요. 영문과 숫자, 일부 특수문자(._-) 사용 가능. 2-10자 길이").removeClass("is-safe").addClass("is-danger")
        $("#input-username").focus()
        return;
    }
    $("#help-id").addClass("is-loading")
    $.ajax({
        type: "POST",
        url: "/sign_up/checkDup",
        data: {
            id: username
        },
        success: function (response) {
            if (response["exists"]) {
                $("#help-id").text("이미 존재하는 아이디입니다.").removeClass("is-safe").addClass("is-danger")
                $("#input-username").focus()
            } else {
                $("#help-id").text("사용할 수 있는 아이디입니다.").removeClass("is-danger").addClass("is-success")
            }
            $("#help-id").removeClass("is-loading")
        }
    });
}
```

-게시글 작성 시간 표현방법 변경(js의 Date() → 알아보기 쉬운 표현)
```javascript
function time2str(date) {
    let today = new Date()
    let time = (today - date) / 1000 / 60 + 540

    if (time < 60) {
        return parseInt(time) + "분 전"
    }
    time = time / 60 // 시간
    if (time < 24) {
        return parseInt(time) + "시간 전"
    }
    time = time / 24
    if (time < 24) {
        return parseInt(time) + "일 전"
    }
    return date.getFullYear() + '년', date.getMonth() + 1 + '월', date.getDate() + '일'
}
```

-게시글 작성 최신순 정렬 기능
```python
@app.route('/lastes')
def lastes():
    lastes = list(db.contents.find({}, {'_id': False}).sort('uploadingTime', -1))
    return jsonify({'all_contents': lastes})
```

#### 2. 실제 서비스 모습
[시연 영상 YouTube 이동](https://www.youtube.com/watch?v=UcEe40xzBko)

#### 3. 회고 및 피드백
- 사용자 친화적인 웹서비스를 개발하기 위해서 '내가 사용자라면 이런 기능이 있으면 좋겠다' 싶은 것들을 많이 고민했습니다.
- 코딩에 친숙하지 않은 사람들이 만나 주어진 개발 시간 내 가능한 개발의 수준이 어느정도인지 감이 잡히지 않은 채 목표를 설정하는 실수도 있었습니다.
- 최초 목표했던 기능들을 모두 구현하지는 못했지만, `이론보다는 일단 당장 개발해보자` 는 모토를 갖고 협업하여 어떻게든 좋은 서비스를 완성해 배포해보는 좋은 경험이었습니다.
- 이미지/버튼 배치하는 것, 간단한 기능을 구현하는 것에도 개발자들의 수많은 고민이 필요했다는 것을 깨달았습니다.
- 좋아요 기능을 끝까지 포기하지 않고 적용하려고 했으나 시간 내 완성할 수 없을 것 같아서 포기했습니다. 다음에 꼭 다시 도전해보고 싶은 기능입니다.
- 전반적인 API의 흐름, 각개 기능이 서버와 클라이언트 간 어떻게 통신한 결과인지 등의 큰 흐름을 이해할 수 있었습니다. 
- 팀 내에서 어떻게 역할을 분배할지, 주어진 조건 대비 실현 가능한 목표는 어느정도인지, 협업 간 GitHub은 어떻게 활용할지를 고민해볼 수 있었습니다.
