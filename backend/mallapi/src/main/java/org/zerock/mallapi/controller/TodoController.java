package org.zerock.mallapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.TodoDTO;
import org.zerock.mallapi.service.TodoService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/todo")
public class TodoController {
    private final TodoService service;
    //조회
    @GetMapping("/{tno}")
    public TodoDTO get(@PathVariable(name = "tno") Long tno){
        return service.get(tno);
    }

    //조회(리스트)
    @GetMapping("/list")
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO){
        log.info("Controller에서 받은 페이지 정보 : "+pageRequestDTO);
        return service.list(pageRequestDTO);
    }

    //등록
    @PostMapping("/")
    public Map<String, Long> register(@RequestBody TodoDTO todoDTO){
        log.info("Controller 등록 dto:"+todoDTO);
        Long tno = service.register(todoDTO);
        return Map.of("TNO", tno);
    }

    //수정
    @PutMapping("/{tno}")
    public Map<String, String> modify(@PathVariable(name = "tno") Long tno, @RequestBody TodoDTO todoDTO){
        log.info("Controller 변경 받은 TNO:"+tno);
        log.info("Controller 변경 받은 dto:"+todoDTO);
        todoDTO.setTno(tno);

        service.modify(todoDTO);
        return Map.of("RESULT", "SUCCESS");
    }

    //삭제
    @DeleteMapping("/{tno}")
    public Map<String, String> remove(@PathVariable(name = "tno") Long tno){
        log.info("Controller 삭제 받은 TNO:"+tno);
        service.remove(tno);
        return Map.of("RESULT", "SUCCESS");
    }
}
