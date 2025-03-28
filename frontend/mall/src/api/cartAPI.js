import {API_SERVER_HOST} from "./todoApi"
import jwtAxios from "../util/jwtUtil"
//사용자 로그인 정보 사용하기 때문에 jwtAxios 사용

const host = `${API_SERVER_HOST}/api/cart`

//카트 목록조회 : get /items
export const getCartItems = async () =>{
    const res = await jwtAxios.get(`${host}/items`)
    return res.data
}

//상품추가, 변경 : post /change
export const postChangeCart = async (cartItem) =>{
    const res = await jwtAxios.post(`${host}/change`,cartItem)
    return res.data
}

//장바구니 아이템 삭제 : delete /{cino}