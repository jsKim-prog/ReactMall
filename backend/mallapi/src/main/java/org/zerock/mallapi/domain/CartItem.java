package org.zerock.mallapi.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "cart")
@Table(name = "tbl_cart_item",
indexes = {@Index(name = "idx_cartitem_cart", columnList = "cart_cno"), @Index(name = "idx_cartitem_pno_cart", columnList = "product_pno, cart_cno")})
public class CartItem {//장바구니 아이템
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cino;
    
    @ManyToOne
    @JoinColumn(name = "product_pno")
    private Product product;
    
    @ManyToOne
    @JoinColumn(name = "cart_cno")
    private Cart cart;
    
    private int qty; //수량
    
    //method
    public void changeQty(int qty){
        this.qty = qty;
    }
}
