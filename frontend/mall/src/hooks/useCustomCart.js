import { cartState } from "../atoms/cartState";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { getCartItems, postChangeCart } from "../api/cartAPI";
import { useEffect } from "react";
import { useAtom } from "jotai";

//Recoil, React-Query 적용
const useCustomCart = ()=>{
    const [cartItems, setCartItems] = useAtom(cartState)
    const queryClient = useQueryClient()
    const changeMutation = useMutation({
        mutationFn:(param)=>postChangeCart(param),
        onSuccess:(result)=>{setCartItems(result)}
    })
    const query = useQuery({
        queryKey:["cart"],
        queryFn:getCartItems,
        options:{
            staleTime:1000*60*60 //1hour
        }
    })

    useEffect(()=>{
        if(query.isSuccess || changeMutation.isSuccess){
            queryClient.invalidateQueries("cart")
            setCartItems(query.data)
        }
    }, [query.isSuccess, query.data])


    const changeCart = (param) =>{
        changeMutation.mutate(param)
    }

    return {cartItems, changeCart}
}

export default useCustomCart;