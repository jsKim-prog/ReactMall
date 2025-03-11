package org.zerock.mallapi.service;

import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.TodoDTO;

public interface TodoService {
    //등록
    Long register(TodoDTO todoDTO);
    //조회(1개)
    TodoDTO get(Long tno);
    //수정
    void modify(TodoDTO todoDTO);
    //삭제
    void remove(Long tno);

    //리스트 조회(페이징 처리)
    PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO);
}
