import { atom } from "jotai"
import { atomWithReset} from "jotai/utils"


//Primitive atoms (read, write, result)
export const cartState = atomWithReset([])


const totalCalculator = (arr)=>{
    const initialValue = 0

    const total = arr.reduce((total, current) => total + current.price * current.qty, initialValue)
    return total
}
//Derived atoms : 파생 atom, 다른 atom을 읽어 자신의 atom 값을 반환
export const cartTotalState = atom((get)=> {
    const arr = get(cartState)
    return totalCalculator(arr)
})