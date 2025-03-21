package org.zerock.mallapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.mallapi.domain.CartItem;
import org.zerock.mallapi.dto.CartItemListDTO;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    //사용자 email-> 사용자의 모든 장바구니 아이템 조회(로그인시):CartItemListDTO: email-> CartItemListDTO
    @Query("select " +
            "new org.zerock.mallapi.dto.CartItemListDTO(ci.cino, ci.qty, p.pno, p.pname, p.price, pi.fileName) " +
            "from CartItem ci inner join Cart mc on ci.cart = mc " +
            "left join Product p on ci.product=p " +
            "left join p.imageList pi " +
            "where mc.owner.email=:email and pi.ord=0 " +
            "order by ci desc")
    public List<CartItemListDTO> getItemsOfCartByEmail(@Param("email") String email);

    //email + pno(상품번호) -> 장바구니 아이템(기존 장바구니 아이템인지 확인) : email + pno -> CartItem
    @Query("select ci " +
            "from CartItem ci inner join Cart c on ci.cart=c " +
            "where c.owner.email=:email and ci.product.pno=:pno")
    public CartItem getItemOfPno(@Param("email")String email, @Param("pno") Long pno);


    // 장바구니 아이템이 속한  장바구니 번호(cno) 찾기(아이템 삭제 후 해당 아이템이 속해 있는 장바구니의 나머지 아이템 찾기): cino -> cno
    @Query("select c.cno " +
            "from Cart c inner join CartItem ci on ci.cart=c " +
            "where ci.cino=:cino")
    public Long getCartFromItem(@Param("cino")Long cino);

    // 장바구니번호(cno) -> 모든 장바구니 아이템 조회 : cno -> CartItemListDTO
    @Query("select " +
            "new org.zerock.mallapi.dto.CartItemListDTO(ci.cino, ci.qty, p.pno, p.pname, p.price, pi.fileName) " +
            "from CartItem ci inner join Cart mc on ci.cart=mc " +
            "left join Product p on ci.product=p " +
            "left join p.imageList pi " +
            "where mc.cno=:cno and pi.ord=0 " +
            "order by ci desc")
    public List<CartItemListDTO> getItemsofCartDTOByCart(@Param("cno") Long cno);
}
