![header](https://capsule-render.vercel.app/api?type=soft&height=120&color=gradient&customColorList=2&text=ReactMall&desc=[Study]코드로%20배우는%20리액트&fontSize=50&fontAlignY=41&descAlignY=81)

## 🔖 개발환경설정
### Frontend
1. node.js : v22.13.1
2. VSCode 설치
3. React plugin 설정 : Simple React Snippets
4. 프로젝트 생성(cmd → 프로젝트 생성할 폴더로 이동)
   ```system
   npx create-react-app 프로젝트폴더명
   ```
5. CSS 설정(Tailwind CSS)
   * install
     ```system
     npm install -D tailwindcss@3.4.17 postcss autoprefixer
     ```
   * Tailwindcss 설정
     ```system
     npx tailwindcss init
     ```
     - tailwind.config.js, index.css 수정
   * Tailwind Css InteliSense 설치(자동완성 위함)
6. React Module 설치
   ```system
   npm install react-router-dom
   ```
### Backend
1. MariaDB 설치 : MariaDB 10.11.11
2. database 및 사용자 생성
   ```sql
   CREATE DATABASE malldb;
   CREATE USER 'user'@'localhost' IDENTIFIED BY 'password';
   CREATE USER 'user'@'%' IDENTIFIED BY 'password';
   GRANT ALL PRIVILEGES ON malldb.* TO 'user'@'localhost';
   GRANT ALL PRIVILEGES ON malldb.* TO 'user'@'%';```

3. 프로젝트 생성(Spring Initializer 활용)
   * Gradle-Groovy / Java
   * Spring Boot : 3.4.3
   * Jar / Java 17
   * Spring Boot Dev Tools, Lombok, Spting Web, Spting Data JPA, MariaDB Driver

## 🔖 React-Router
### ``<Outlet>``
* 중첩적으로 라우팅이 적용될 때 기존 컴포넌트의 구조 유지
* 하위 경로의 페이지 컴포넌트 제작 시 IndexPage 구조 유지
* 참고 : src\pages\todo\IndexPage.js
### ``useParams()``
* src\pages\todo\ReadPage.js
* 주소창의 경로 사용, 변수로 전달되는 값을 추출해서 출력
### ``useSearchParams()``
* src\pages\todo\ListPage.js
* 경로에서 ?이후 나오는 쿼리스트링 값 추출
### ``useNavigate()``
* src\pages\todo\IndexPage.js
* 고정된 링크 이동 : ``<Link>``
* 동적인 데이터 처리 이동 : ``useNavigate()``
### ``createSearchParams()``
* 쿼리스트링 만들기
* src\pages\todo\ReadPage.js
* 페이지 이동시 쿼리스트링 유지
  : ``useSearchParams()``으로 전달된 쿼리스트링을 확인하여 ``createSearchParams()``으로 쿼리스트링을 만들어 ``navigate()``에 실어 보낸다.


