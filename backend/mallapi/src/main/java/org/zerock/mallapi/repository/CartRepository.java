package org.zerock.mallapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.mallapi.domain.Cart;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    //사용자 email -> cart 정보
    @Query("select cart from Cart cart where cart.owner.email=:email")
    public Optional<Cart> getCartOfMember(@Param("email")String email);
}
