package org.zerock.mallapi.repository;

import jakarta.transaction.Transactional;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.mallapi.domain.Product;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductRepositoryTests {
    @Autowired
    ProductRepository productRepository;

    @Test
    public void testInsert(){
        for (int i=0;i<10;i++){
            Product product = Product.builder()
                    .pname("상품"+i)
                    .price(100*i)
                    .pdesc("상품설명..."+i)
                    .build();
            //이미지 파일 추가(2개)
            product.addImageString(UUID.randomUUID().toString()+"_"+"IMAGE1.jpg");
            product.addImageString(UUID.randomUUID().toString()+"_"+"IMAGE2.jpg");
            productRepository.save(product);
        }
    }

    //이미지 조회
    @Transactional
    @Test
    public void testRead(){
        Long pno = 1L;
        Optional<Product> result = productRepository.findById(pno);
        Product product = result.orElseThrow();

        log.info("Test Product 객체:"+product);
//        Hibernate:
//        select
//        p1_0.pno,
//                p1_0.del_flag,
//                p1_0.pdesc,
//                p1_0.pname,
//                p1_0.price
//        from
//        tbl_product p1_0
//        where
//        p1_0.pno=?
        log.info("Test Product 파일:"+product.getImageList());
//        Hibernate:
//        select
//        il1_0.product_pno,
//                il1_0.file_name,
//                il1_0.ord
//        from
//        product_image_list il1_0
//        where
//        il1_0.product_pno=?
    }

    @Test
    public void testRead2(){
        Long pno = 1L;
        Optional<Product> result = productRepository.selectOne(pno);
        Product product = result.orElseThrow();

        log.info("Test Product 객체:"+product);
        log.info("Test Product 파일:"+product.getImageList());
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
    }

    @Commit //DB 반영
    @Transactional //update, delete 에서는 필수(@Modify)
    @Test
    public void testDelete(){
        Long pno = 2L;
        productRepository.updateToDelete(pno,true);
//        Hibernate:
//        update
//        tbl_product p1_0
//        set
//        del_flag=?
//                where
//        p1_0.pno=?
    }

    //상품변경 : 이미지는 첨부파일 리스트 비우고 다시 추가
    @Test
    public void testUpdate(){
        Long pno = 3L;

        Product product = productRepository.selectOne(pno).get();
        product.changeName("3번상품 수정");
        product.changeDesc("3번상품 변경설명");
        product.changePrice(5000);

        //첨부파일 수정
        product.clearList();
        product.addImageString("NewImage1.jpg");
        product.addImageString("NewImage2.jpg");
        product.addImageString("NewImage3.jpg");
        productRepository.save(product);

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
//                Hibernate:
//                select
//        p1_0.pno,
//                p1_0.del_flag,
//                p1_0.pdesc,
//                p1_0.pname,
//                p1_0.price
//        from
//        tbl_product p1_0
//        where
//        p1_0.pno=?
//                Hibernate:
//                select
//        il1_0.product_pno,
//                il1_0.file_name,
//                il1_0.ord
//        from
//        product_image_list il1_0
//        where
//        il1_0.product_pno=?
//                Hibernate:
//                update
//        tbl_product
//                set
//        del_flag=?,
//                pdesc=?,
//                pname=?,
//                price=?
//                        where
//        pno=?
//                Hibernate:
//                delete
//        from
//                product_image_list
//        where
//        product_pno=?
//                Hibernate:
//                insert
//        into
//                product_image_list
//        (product_pno, file_name, ord)
//        values
//                (?, ?, ?)
//        Hibernate:
//        insert
//                into
//        product_image_list
//                (product_pno, file_name, ord)
//        values
//                (?, ?, ?)
//        Hibernate:
//        insert
//                into
//        product_image_list
//                (product_pno, file_name, ord)
//        values
//                (?, ?, ?)
    }

    //리스트 조회(+이미지 포함)
    @Test
    public void testList(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);

        result.getContent().forEach(arr-> log.info(Arrays.toString(arr)));
//        Hibernate:
//        select
//        p1_0.pno,
//                p1_0.del_flag,
//                p1_0.pdesc,
//                p1_0.pname,
//                p1_0.price,
//                il1_0.file_name,
//                il1_0.ord
//        from
//        tbl_product p1_0
//        left join
//        product_image_list il1_0
//        on p1_0.pno=il1_0.product_pno
//        where
//        il1_0.ord=0
//        and p1_0.del_flag=0
//        order by
//        p1_0.pno desc
//        limit
//                ?
    }

}
