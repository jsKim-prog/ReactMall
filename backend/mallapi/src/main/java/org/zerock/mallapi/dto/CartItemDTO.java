package org.zerock.mallapi.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private String email;
    private Long pno; //상품정보
    private int qty; //수량
    private Long cino; //장바구니 아이템 정보
}
