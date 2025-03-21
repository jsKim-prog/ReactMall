package org.zerock.mallapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.mallapi.domain.Cart;
import org.zerock.mallapi.domain.CartItem;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.domain.Product;
import org.zerock.mallapi.dto.CartItemDTO;
import org.zerock.mallapi.dto.CartItemListDTO;
import org.zerock.mallapi.repository.CartItemRepository;
import org.zerock.mallapi.repository.CartRepository;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO) {
        //cartItemDTO data
        String email = cartItemDTO.getEmail();
        Long pno = cartItemDTO.getPno();
        int qty = cartItemDTO.getQty();
        Long cino = cartItemDTO.getCino();
        //cino 있음 -> qty만 변경
        if(cino!=null){
            Optional<CartItem> cartItemResult = cartItemRepository.findById(cino);
            CartItem cartItem = cartItemResult.orElseThrow();
            cartItem.changeQty(qty);
            cartItemRepository.save(cartItem);
            return getCartItems(email);
        }
        //cino 없음
        //1. 사용자 카트먼저 찾기
        Cart cart = getCart(email);
        CartItem cartItem = null;
        //2. 사용자 카트의 동일상품 검색
        cartItem = cartItemRepository.getItemOfPno(email,pno);
        if(cartItem==null){
            Product product = Product.builder().pno(pno).build();
            cartItem = CartItem.builder()
                    .product(product).cart(cart).qty(qty)
                    .build();
        }else {
            cartItem.changeQty(qty);
        }
        //3. 상품아이템 저장
        cartItemRepository.save(cartItem);

        return getCartItems(email);
    }

    @Override
    public List<CartItemListDTO> getCartItems(String email) {
        return cartItemRepository.getItemsOfCartByEmail(email);
    }

    @Override
    public List<CartItemListDTO> remove(Long cino) {
        Long cno = cartItemRepository.getCartFromItem(cino);
        log.info("Service---remove cart no:"+cno);
        cartItemRepository.deleteById(cino);
        return cartItemRepository.getItemsofCartDTOByCart(cno);
    }

    //getCart : 장바구니가 없는 경우 새로운 장바구니 생성하고 반환
    private Cart getCart(String email){
        Cart cart = null;
        Optional<Cart> result = cartRepository.getCartOfMember(email);
        if(result.isEmpty()){
            log.info("Service---Cart of the Member is not exist!!");
            Member member = Member.builder().email(email).build();
            Cart tempCart = Cart.builder().owner(member).build();
            cart = cartRepository.save(tempCart);
        }else {
            cart=result.get();
        }
        return cart;
    }
}
