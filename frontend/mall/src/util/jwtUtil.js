import axios from "axios";
import { getCookie, setCookie } from "./cookieUtil";
import { API_SERVER_HOST } from "../api/todoApi";

//Axios 요청, 응답 시 동작할 함수들
const jwtAxios = axios.create()

//Token 갱신 :Access Token, Refresh Token을 사용하여 api/member/refresh-> refresh(header, refreshToken) 호출
const refreshJWT = async (accessToken, refreshToken) =>{
    const host = API_SERVER_HOST
    const header = {headers: {"Authorization": `Bearer ${accessToken}`}}

    const res = await axios.get(`${host}/api/member/refresh?refreshToken = ${refreshToken}`, header)

    console.log("---------")
    console.log(res.data)

    return res.data
}


//before request--Authorization  헤더 추가(Access Token)
const beforeReq = (config) =>{
    console.log("before request.....")
    const memberInfo = getCookie("member")

    if(!memberInfo){ //로그인 쿠키가 없다면 예외발생
        console.log("Member Not FOUND")
        return Promise.reject(
            {response:{
                data:{error:"REQUIRE_LOGIN"}
            }}
        )
    }

    //Authorization  헤더 추가(Access Token)
    const {accessToken} = memberInfo

    config.headers.Authorization = `Bearer ${accessToken}`
    
    return config
}

//fail request
const requestFail = (err) => {
    console.log("request error......")
    return Promise.reject(err)
}

//before return response
const beforeRes  = async (res) =>{
    console.log("before return response.......")
    //console.log(res)
    //ERROR_ACCESS_TOKEN : 토큰 관련 메시지인 경우 Refresh Token으로 다시 호출
    const data = res.data
    if(data && data.error ==='ERROR_ACCESS_TOKEN'){
        const memberCookieValue = getCookie("member")
        const result = await refreshJWT(memberCookieValue.accessToken, memberCookieValue.refreshToken)
        console.log("refreshJWT RESULT : ", result)

        memberCookieValue.accessToken = result.accessToken
        memberCookieValue.refreshToken = result.refreshToken

        setCookie("member", JSON.stringify(memberCookieValue), 1)

        //원래의 호출 다시 되돌리기
        const originalRequest = res.config
        originalRequest.headers.Authorization = `Bearer ${result.accessToken}`

        return await axios(originalRequest)
    }
    return res
}

//fail response
const responseFail = (err) =>{
    console.log("response fail error.......")
    return Promise.reject(err)
}

jwtAxios.interceptors.request.use(beforeReq, requestFail)

jwtAxios.interceptors.response.use(beforeRes, responseFail)

export default jwtAxios;