package org.zerock.mallapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zerock.mallapi.dto.CartItemDTO;
import org.zerock.mallapi.dto.CartItemListDTO;
import org.zerock.mallapi.service.CartService;

import java.security.Principal;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    //장바구니 아이템 수정, 추가
    @PreAuthorize("#itemDTO.email == authentication.name")
    @PostMapping("/change")
    public List<CartItemListDTO> changeCart(@RequestBody CartItemDTO itemDTO){
        log.info("Controller---"+itemDTO);
        //수량이 0보다 작으면 장바구니 아이템 삭제
        if(itemDTO.getQty()<=0){
            return cartService.remove(itemDTO.getCino());
        }
        return cartService.addOrModify(itemDTO);
    }

    //장바구니 목록 조회
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/items")
    public List<CartItemListDTO> getCartItems(Principal principal){
        //Principal : 현재 사용자 정보에 접근하기 위해
        String email = principal.getName();
        log.info("Controller--principal.email:"+email);
        return cartService.getCartItems(email);
    }

    //장바구니 아이템 삭제
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @DeleteMapping("/{cino}")
    public List<CartItemListDTO> removeFromCart(@PathVariable("cino") Long cino){
        log.info("Controller----remove cino:"+cino);
        return cartService.remove(cino);
    }
}
