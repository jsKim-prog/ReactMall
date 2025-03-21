package org.zerock.mallapi.repository;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.zerock.mallapi.domain.Cart;
import org.zerock.mallapi.domain.CartItem;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.domain.Product;
import org.zerock.mallapi.dto.CartItemListDTO;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class CartRepositoryTests {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    //장바구니 아이템 입력
    @Transactional
    @Commit
    @Test
    public void testInsertByProduct(){
        log.info("Cart 입력 테스트--------");
        //사용자 전송 정보
        String email = "user1@aaa.com";
        Long pno = 5L;
        int qty = 2;

        //기존 사용자의 장바구니 아이템 여부 확인
        CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);
        if(cartItem !=null){
            cartItem.changeQty(qty);
            cartItemRepository.save(cartItem);
            return;
        }
        //장바구니 아이템이 없다면 장바구니부터 확인
        //1. 장바구니를 만든적이 있는지?
        Optional<Cart> result = cartRepository.getCartOfMember(email);
        Cart cart = null;
        //2. 장바구니 없으면 장바구니 생성
        if(result.isEmpty()){
            log.info("장바구니가 존재하지 않습니다.");
            Member member = Member.builder().email(email).build();
            Cart tempCart = Cart.builder().owner(member).build();
            cart = cartRepository.save(tempCart);
        }else {
            cart = result.get();
        }
        log.info("cart:"+cart);

        //3. 카트아이템이 없으면 CartItem 객체생성
        if(cartItem==null){
            Product product = Product.builder().pno(pno).build();
            cartItem = CartItem.builder().product(product).cart(cart).qty(qty).build();
        }
        //4. 상품아이템 저장
        cartItemRepository.save(cartItem);
    }
//    Hibernate:
//    select
//    ci1_0.cino,
//    ci1_0.cart_cno,
//    ci1_0.product_pno,
//    ci1_0.qty
//            from
//    tbl_cart_item ci1_0
//    join
//    tbl_cart c1_0
//    on ci1_0.cart_cno=c1_0.cno
//            where
//    c1_0.member_owner=?
//    and ci1_0.product_pno=?
//    Hibernate:
//    select
//    c1_0.cno,
//    c1_0.member_owner
//            from
//    tbl_cart c1_0
//    where
//    c1_0.member_owner=?
//            2025-03-21T19:01:52.960+09:00  INFO 14352 --- [mallapi] [    Test worker] o.z.m.repository.CartRepositoryTests     : 장바구니가 존재하지 않습니다.
//            Hibernate:
//    select
//        null,
//    m1_0.nickname,
//    m1_0.pw,
//    m1_0.social
//            from
//    member m1_0
//    where
//    m1_0.email=?
//    Hibernate:
//    insert
//            into
//    tbl_cart
//            (member_owner)
//    values
//            (?)
//2025-03-21T19:01:53.101+09:00  INFO 14352 --- [mallapi] [    Test worker] o.z.m.repository.CartRepositoryTests     : cart:Cart(cno=1)
//    Hibernate:
//    insert
//            into
//    tbl_cart_item
//            (cart_cno, product_pno, qty)
//    values
//            (?, ?, ?)

    //장바구니아이템 수정 테스트(수량조정)
    @Test
    @Commit
    public void testUpdateByCino(){
        Long cino = 1L;
        int qty = 4;
        Optional<CartItem> result = cartItemRepository.findById(cino);

        CartItem cartItem = result.orElseThrow();
        cartItem.changeQty(qty);
        cartItemRepository.save(cartItem);
//        Hibernate:
//        select
//        ci1_0.cino,
//                c1_0.cno,
//                o1_0.email,
//                o1_0.nickname,
//                o1_0.pw,
//                o1_0.social,
//                p1_0.pno,
//                p1_0.del_flag,
//                p1_0.pdesc,
//                p1_0.pname,
//                p1_0.price,
//                ci1_0.qty
//        from
//        tbl_cart_item ci1_0
//        left join
//        tbl_cart c1_0
//        on c1_0.cno=ci1_0.cart_cno
//        left join
//        member o1_0
//        on o1_0.email=c1_0.member_owner
//        left join
//        tbl_product p1_0
//        on p1_0.pno=ci1_0.product_pno
//        where
//        ci1_0.cino=?
//                Hibernate:
//                select
//        ci1_0.cino,
//                c1_0.cno,
//                o1_0.email,
//                o1_0.nickname,
//                o1_0.pw,
//                o1_0.social,
//                p1_0.pno,
//                p1_0.del_flag,
//                p1_0.pdesc,
//                p1_0.pname,
//                p1_0.price,
//                ci1_0.qty
//        from
//        tbl_cart_item ci1_0
//        left join
//        tbl_cart c1_0
//        on c1_0.cno=ci1_0.cart_cno
//        left join
//        member o1_0
//        on o1_0.email=c1_0.member_owner
//        left join
//        tbl_product p1_0
//        on p1_0.pno=ci1_0.product_pno
//        where
//        ci1_0.cino=?
//                Hibernate:
//                update
//        tbl_cart_item
//                set
//        cart_cno=?,
//                product_pno=?,
//                qty=?
//                        where
//        cino=?
    }

    //장바구니 목록조회
    @Test
    public void testListOfMember(){
        String email = "user1@aaa.com";
        List<CartItemListDTO> cartItemList = cartItemRepository.getItemsOfCartByEmail(email);

        for(CartItemListDTO listDTO:cartItemList){
            log.info(listDTO);
        }
//        Hibernate:
//        select
//        ci1_0.cino,
//                ci1_0.qty,
//                p1_0.pno,
//                p1_0.pname,
//                p1_0.price,
//                il1_0.file_name
//        from
//        tbl_cart_item ci1_0
//        join
//        tbl_cart c1_0
//        on ci1_0.cart_cno=c1_0.cno
//        left join
//        tbl_product p1_0
//        on ci1_0.product_pno=p1_0.pno
//        left join
//        product_image_list il1_0
//        on p1_0.pno=il1_0.product_pno
//        where
//        c1_0.member_owner=?
//                and il1_0.ord=0
//        order by
//        ci1_0.cino desc
    }

    //아이템 삭제 및 삭제 후 목록반환
    //아이템 삭제 전 장바구니 번호(cno) 구하기 -> 삭제 후 목록 구하기(cno)
    @Test
    public void testDeleteThenList(){
        Long cino = 1L;
        //장바구니 번호 구하기
        Long cno = cartItemRepository.getCartFromItem(cino);
        //아이템 삭제(테스트시에는 주석처리)
        //cartItemRepository.deleteById(cino);

        //장바구니 목록구하기
        List<CartItemListDTO> cartItemList = cartItemRepository.getItemsofCartDTOByCart(cno);
        for (CartItemListDTO listDTO:cartItemList){
            log.info(listDTO);
        }
//        Hibernate:
//        select
//        c1_0.cno
//                from
//        tbl_cart c1_0
//        join
//        tbl_cart_item ci1_0
//        on ci1_0.cart_cno=c1_0.cno
//        where
//        ci1_0.cino=?
//                Hibernate:
//                select
//        ci1_0.cino,
//                ci1_0.qty,
//                p1_0.pno,
//                p1_0.pname,
//                p1_0.price,
//                il1_0.file_name
//        from
//        tbl_cart_item ci1_0
//        join
//        tbl_cart c1_0
//        on ci1_0.cart_cno=c1_0.cno
//        left join
//        tbl_product p1_0
//        on ci1_0.product_pno=p1_0.pno
//        left join
//        product_image_list il1_0
//        on p1_0.pno=il1_0.product_pno
//        where
//        c1_0.cno=?
//                and il1_0.ord=0
//        order by
//        ci1_0.cino desc
    }


}
