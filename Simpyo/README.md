# 2018년 서울시 앱 공모전 출품작(Simpyo)

## Package

- __Activity__
    - LoadingActivity   (1)
    - LoginActivity     (1)
    - RegisterActivity      (1)
    - ReportActivity    (4)
    - ReportDoneActivity    (4)    
    - ReportListActivity    (4)
    - ReportMyListActivity  (4)
    - SeeReportActivity (5)
    - SetReportActivity (5)
    - TutorialActivity  (6)
- __Fragment__
    - Fragment1 (4)
    - Fragment2 (4)
    - MyFragment1   (4)
    - MyFragment2   (4)
    - TabPagerAdapter   (4)
    - MyTabPagerApdater (4)
- __GoogleMap__
    - GoogleMapFragment (3)
    - `MapActivity` (모두다)
- __HttpRequest__
    - PostHttp  (1)
- __Model__
    - LoginModel    (1)
- __RecyclerView__
    - Report    (4)
    - ReportListAdpater (4)
- __Retrofit__
    - NetRetrofit   (4)
    - RetrofitService   (4)
    - NoSmokePin    (4)
    - SmokePin  (4)
    - YellowPin (4)
- __Setting__
    - FontChange    (2)
    - GetPermission
    - ServerURL (2)
    - Setting   (2)
    - ThreadSleeep
- __ViewPager__
    - FragmentFirst (6)
    - FragmentSecond (6)
    - FragmentThird (6)
    - ViewPagerAdpater  (6)


## 개발 순서

1. __스플래시, 로그인, 회원가입 구현, 로그인 모델__
    스플래시로 첫 로딩화면을 보여준 후 
    -> 자동로그인일 경우 맵으로 이동
    -> 자동로그인이 아닐경우 로그인 화면으로 이동 (추후에 튜토리얼로 변경)

    - LoginModel
        - 기본적인 로그인, 회원가입은 여기서 Class 로 분리
        - 자동로그인, 회원가입 가져오기
        - 이메일, 비밀번호, 이름 저장 (SharedPreferences로)
        - 비밀번호는 서버에서 SHA-256 으로 암호화
    - PostHttp
        - AsyncTask 쓰레드 사용
        - POST 방식으로 통신, 안드로이드 기본적인 Http 통신 방법을 사용함

2. __Setting__
    - FontChange : 액티비티내 전체 폰트 변경, View 를 파라미터로 받아와서 Bold, ExtraBold 폰트로 변경하는 코드
    - Setting : LoadingActivity 의 로딩화면, 애니메이션 작동하는 코드, Log Tag, 아이콘 크기 등등 저장되어있다.
    - ServerURL : PostHttp 에서 필요한 ServerURL 를 public static final String 으로 선언

3. __구글맵__
    - GoogleMpaFragment : 구글맵을 띄우는 Fragment, MapActivity 에 Sync 해서 코드가 별로 없다.
    - __`MapActivity`__ : 코드의 반 이상이 여기에 담겨져 있다고 해도 과언이 아니다.
    여기서 모든걸 보여줘야하고, 분리를 한다고 했는데도 분리가 잘 되지 않았다.

4. __`Retrofit2`, 신고하기, 신고목록__
    - Retrofit2 라이브러리 사용
        - NetRetrofit 
        Django 서버 호스트 작성해주고, `SingleTon`으로 객체를 가져와준다.
        - RetrofitService
        각각 필요에 따라 서버 통신을 달리 해준다.
        - NoSmokePin, SmokePin, YellowPin
        GSON Converter 를 통해서 JSON 파싱없이 바로 객체로 들어감
    - 신고하기 : 
        - MapActivity 통해서 위도, 경도, 주소지 이름 가져오고, View 에 등록
        - 신고내용 적고 완료 누르면 -> 홈으로 갈지, 신고목록을 볼지 선택하는 화면 나옴
    - 신고목록 : 일반 사용자와 관리자를 분리함 (일반 : My가 붙어있음)
        - Fragment1, Fragment2 :
        Retrofit2 를 통해서 신고한 목록을 가져와준다. (관리자)
        RecyclerView를 통해서 신고한 목록들을 보여준다.
        - ReportListActvitiy
        TabLayout을 통해서 Fragment1, Fragment2 를 씌워주고, ViewPager 등록
        - My 도 동일


5. __신고 상세보기, 신고 처리하기__
    - SeeReportActivity 
        - 리사이클러뷰로부터 정보를 가져오고, 처리가 됐는지 안됐는지 상세하게 보여준다.
            신고내용 또한 상세하게 보여준다.

    - SetReportActivity 
        - 관리자만 들어갈 수 있다.
        - 신고를 처리, 완료할 수 있다.

6. __튜토리얼__
    - 로그인을 한적이 없을경우 나온다.
    - 권한 받아오는걸 로그인 화면으로 이동
    - 로그인 기록이 있으면 뜨지 않는다.


## 개발일지

### ~ 2018.09.22

### `DID IT!`

- 신고폼 리스트 데이터베이스 생성
- 신고폼 View 생성
- 신고폼 리스트 서버로부터 가져오기
- 신고 기능 생성
- 관리자, 일반 구분지어서 신고목록 가져오기

### `TO DO LIST!`

- 리스트 클릭시 뷰 보여지는거
- 신고 완료 버튼 누를시 View
- 세세한 디자인 UI

### ~ 2018.09.21

#### `DID IT!`

- 로그인, 회원가입 서버 연동
- 관리자 구분짓기 성공
- `MapActivity` 들어올때마다 관리자인지 아닌지 받아온다.
- 사용자가 보고 있는 화면 범위부분만 서버에서 핀을 가져온다.
- 사용자가 카메라를 움직이고, 움직임을 멈췄을때 사용자가 보고있는 화면범위 부분만 서버에서 핀을 가져온다.
- 노란 핀 선택하고 추가, 서버에서 가져오기 기능 추가

#### `TO DO LIST!`

- 카메라 확대 정도 고정
- 시작할때 현재 위치 가져오고, 위치 옮기는거 못하나,,,?
- 흡연시설 핀 (이거는 데이터베이스에 데이터만 가공하고, csv를 파이썬을 통해서 넣으면 된다.)
- 신고폼 리스트 데이터베이스 생성
- 신고폼 View 생성
- 신고폼 리스트 서버로부터 가져오기

이제 곧 끝난다!

### ~ 2018.09.17

#### `DID IT!`

- 구글맵 GPS 완성
- 세세한 UI 부분 완성
- 서울시 금연구역 MySQL 데이터베이스에 넣기 (72000개)


### 2018.09.12 ~ 2018.09.13

#### `DID IT!`

- GPS 권한 마쉬멜로 버젼에서 허락받기
- 현재위치 가져오기 
- 현재위치로 카메라 이동 (껏다 켰다 기능해야할듯)
- smoking, no_smoking, pin 구분지을수 있도록 Model 변경 (boolean -> int)
- pin_selected 선택시 올라오게끔 애니메이션 추가

#### `TO DO LIST` 

- 신고 폼 생성
- 좀 더 세세한 디자인 수정
- 현재위치 카메라 이동 -> 계속 되게끔 (해야되나?)
- 나머지 다... 하다보면 되겠지 ㅜㅜ


### 2018.09.11 ~ 2018.09.12

#### `DID IT!`

- 전체적인 틀 완성
- Login, Register (Sign in , Sign Up) 뷰 생성
- Navigation Drawer 추가
- Navigation Drawer 안에 있는 item 들 추가
- 폰트 변경 (네이버 스퀘어.ttf)
- 구글 맵 API 연결
- 구글 Default 마킹 생성, return 하게끔
- Default 핑 찍어둠
- 흡연스팟

#### `TO DO LIST`
- 신고 폼 생성
- 금연존, 흡연존, 전체뷰 버튼 클릭에 따른 visibility 설정
- 현재 나의 Location 퍼미션 받아오기, 퍼미션 사용자에게 요청하기
- 현재 나의 Location 에 따라서 카메라 움직이기
- 확대, 축소 버튼 생성
- 확대, 축소 버튼에 따른 카메라의 확대 축소 변경
- 관리자 폼

#### `I NEED IT!`
- 회원 정보 (Login API)
- 핑 API
- 흡연스팟, 금연 스팟, 등등...다....
- 정확한 서버 설계