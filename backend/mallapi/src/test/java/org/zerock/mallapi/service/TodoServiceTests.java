package org.zerock.mallapi.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.TodoDTO;

import java.time.LocalDate;

@SpringBootTest
@Log4j2
public class TodoServiceTests {
    @Autowired
    private TodoService todoService;

    @Test
    public void testRegister(){
        TodoDTO todoDTO = TodoDTO.builder()
                .title("서비스 테스트")
                .writer("tester")
                .dueDate(LocalDate.of(2025, 3,11))
                .build();
        Long tno = todoService.register(todoDTO);
        log.info("테스트 결과 Tno : "+tno);
        //테스트 결과 Tno : 101
    }

    @Test
    public void testGet(){
        Long tno = 10L;
        TodoDTO dto = todoService.get(tno);
        log.info("테스트결과 dto : "+dto);
        //테스트결과 dto : TodoDTO(tno=10, title=Title...10, writer=user00, complete=false, dueDate=2025-03-10)
    }

    //변경테스트
    @Test
    public void testModify(){
        TodoDTO modDto = TodoDTO.builder()
                .tno(3L)
                .title("변경테스트")
                .dueDate(LocalDate.of(2025,3,11))
                .complete(true)
                .build();
        todoService.modify(modDto);
        TodoDTO dto=todoService.get(modDto.getTno());
        log.info("변경테스트 결과dto:"+dto);
        //변경테스트 결과dto:TodoDTO(tno=3, title=변경테스트, writer=user00, complete=true, dueDate=2025-03-11)
    }

    //삭제테스트
    @Test
    public void TestRemove(){
        todoService.remove(101L);
    }

    //리스트 테스트
    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(2).size(10).build();
        PageResponseDTO<TodoDTO> response = todoService.list(pageRequestDTO);
        log.info("리스트 테스트--------------------------------");
        log.info(response);
        log.info("리스트 테스트 끝--------------------------------");
    }
}
