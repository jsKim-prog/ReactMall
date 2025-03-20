import { createAsyncThunk, createSlice } from "@reduxjs/toolkit"
import { loginPost } from "../api/memberApi"
import { getCookie, removeCookie, setCookie } from "../util/cookieUtil"

const initState = {
    email:''
}

//실행시 쿠키조회 및 로그인 정보 로딩
const loadMemberCookie = ()=>{
    const memberInfo = getCookie("member")
    //닉네임처리
    if(memberInfo && memberInfo.nickname){
        memberInfo.nickname = decodeURIComponent(memberInfo.nickname)
    }

    return memberInfo
}

export const loginPostAsync = createAsyncThunk('loginPostAsync', (param)=>{
    return loginPost(param)
})

const loginSlice = createSlice({
    name:'LoginSlice',
    initialState: loadMemberCookie()||initState, //쿠키가 없다면 초기값 사용
    reducers:{
        login:(state, action) =>{
            console.log("login......")
            
            //소셜로그인 회원 사용
            const payload = action.payload
            setCookie("member", JSON.stringify(payload),1)
            return payload
        },
        logout:(state, action) =>{
            console.log("logout......")
            //쿠키 삭제
            removeCookie("member")
            return {...initState}
        }
    },
    extraReducers:(builder) =>{
        builder.addCase(loginPostAsync.fulfilled, (state, action)=>{
            console.log("fulfilled") //완료
            //API server에서 받은 json 데이터 처리
            const payload = action.payload
            //정상적인 로그인시 쿠키 저장(1일)
            if(!payload.error){
                setCookie("member", JSON.stringify(payload), 1)
            }
            return payload
        })
        .addCase(loginPostAsync.pending, (state, action)=>{
            console.log("pending") //처리중
        })
        .addCase(loginPostAsync.rejected, (state, action)=>{
            console.log("rejected") //에러
        })
    }

})

export const {login, logout} = loginSlice.actions

export default loginSlice.reducer