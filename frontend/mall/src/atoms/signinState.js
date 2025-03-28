import { atom } from "recoil";
import { getCookie } from "../util/cookieUtil";
const initState = {
    email:'',
    nickname:'',
    social:false,
    accessToken:'',
    refreshToken:''
}

//새로고침 대응: 쿠키에서 초기값 체크
const loadMemberCookie = () =>{
    const memberInfo = getCookie("member")

    //닉네임 처리
    if(memberInfo && memberInfo.nickname){
        memberInfo.nickname = decodeURIComponent(memberInfo.nickname)
    }
    return memberInfo
}

//Atom 생성시 초기값 지정 
const signinState = atom({
    key: 'signinState',
    default: loadMemberCookie || initState
})

export default signinState;