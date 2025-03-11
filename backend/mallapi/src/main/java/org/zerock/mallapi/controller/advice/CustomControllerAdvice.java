package org.zerock.mallapi.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class CustomControllerAdvice {
    //존재하지 않는 번호 조회
    //<?> : 모든 클래스, 인터페이트 타입 등 허용(와일드카드)
    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<?> notExist(NoSuchElementException e){
        String msg = e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg",msg));
    }
    //페이지번호를 숫자가 아닌 문자로 전달
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleIllegalArgumentException(MethodArgumentNotValidException e){
        String msg = e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Map.of("msg",msg));
    }

}
