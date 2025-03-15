package org.zerock.mallapi.service;

import jakarta.transaction.Transactional;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;

@Transactional
public interface ProductService {
    //리스트(이미지 포함)
    PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO);

    //등록
    Long register(ProductDTO productDTO);

    //조회
    ProductDTO get(Long pno);

    //수정
    void modify(ProductDTO productDTO);
    //삭제
    void remove(Long pno);
}
