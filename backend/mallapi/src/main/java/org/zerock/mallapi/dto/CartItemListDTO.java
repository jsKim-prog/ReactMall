package org.zerock.mallapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor //생성자 직접정의->JPQL Projection 사용
public class CartItemListDTO { //Controller로 전달되는 사용자의 장바구니에 포함된 상품정보, 수량, 이미지 파일들
    private Long cino;
    private int qty;
    private Long pno;
    private String pname;
    private int price;
    private String imageFile;

    //생성자
    public CartItemListDTO(Long cino, int qty, Long pno, String pname, int price, String imageFile){
        this.cino = cino;
        this.qty = qty;
        this.pno = pno;
        this.pname = pname;
        this.price = price;
        this.imageFile = imageFile;
    }
}
