package org.zerock.mallapi.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductServiceTests {
    @Autowired
    ProductService productService;
    //리스트
    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build(); //1page, 10size 기본
        PageResponseDTO<ProductDTO> result = productService.getList(pageRequestDTO);

        result.getDtoList().forEach(dto-> log.info(dto));
    }

    //등록
    @Test
    public void testRegister(){
        ProductDTO productDTO = ProductDTO.builder()
                .pname("신규상품")
                .pdesc("신규등록 테스트 상품입니다.")
                .price(1000)
                .build();
        String fileName1 = UUID.randomUUID()+"_Test1.jpg";
        String fileName2 = UUID.randomUUID()+"_Test2.jpg";
        productDTO.setUploadFileNames(List.of(fileName1, fileName2));
        Long pno = productService.register(productDTO);
        log.info("등록테스트+신규등록 pno:"+pno);
        //등록테스트+신규등록 pno:11
    }

    //조회
    @Test
    public void testRead(){
        Long pno = 3L;
        ProductDTO productDTO = productService.get(pno);
        log.info("Test get+++++++++++");
        log.info(productDTO);
        log.info(productDTO.getUploadFileNames());
//        Hibernate:
//        select
//        p1_0.pno,
//                p1_0.del_flag,
//                il1_0.product_pno,
//                il1_0.file_name,
//                il1_0.ord,
//                p1_0.pdesc,
//                p1_0.pname,
//                p1_0.price
//        from
//        tbl_product p1_0
//        left join
//        product_image_list il1_0
//        on p1_0.pno=il1_0.product_pno
//        where
//        p1_0.pno=?

//        ProductDTO(pno=3, pname=3번상품 수정, price=5000, pdesc=3번상품 변경설명, delFlag=false, files=[], uploadFileNames=[NewImage1.jpg, NewImage2.jpg, NewImage3.jpg])
        //[NewImage1.jpg, NewImage2.jpg, NewImage3.jpg]
    }
}
