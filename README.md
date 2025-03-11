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
### Backend
1. MariaDB 설치 : MariaDB 10.11.11
2. database 및 사용자 생성
   ```sql
   CREATE DATABASE malldb;
   CREATE USER 'user'@'localhost' IDENTIFIED BY 'password';
   CREATE USER 'user'@'%' IDENTIFIED BY 'password';
   GRANT ALL PRIVILEGES ON malldb.* TO 'user'@'localhost';
   GRANT ALL PRIVILEGES ON malldb.* TO 'user'@'%';
  ```

3. 프로젝트 생성(Spring Initializer 활용)
   * Gradle-Groovy / Java
   * Spring Boot : 3.4.3
   * Jar / Java 17
   * Spring Boot Dev Tools, Lombok, Spting Web, Spting Data JPA, MariaDB Driver

