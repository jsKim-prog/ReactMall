import { useDispatch, useSelector } from "react-redux";
import { getCartItemsAsync, postChangeCartAsync } from "../slices/cartSlice";

const useCustomCart = ()=>{
    const cartItems = useSelector(state=> state.cartSlice)
    const dispatch = useDispatch()
    //로그인 관련 데이터와 장바구니 관련 호출기능을 반환
    const refreshCart = ()=>{
        dispatch(getCartItemsAsync())
    }

    const changeCart = (param) =>{
        dispatch(postChangeCartAsync(param))
    }

    return {cartItems, refreshCart, changeCart}
}

export default useCustomCart;