import { atom } from "jotai";
import { getCookie } from "../util/cookieUtil";

const initState = {
    email: '',
    nickname: '',
    social: false,
    accessToken: '',
    refreshToken: ''
}

//새로고침 대응: 쿠키에서 초기값 체크
const loadMemberCookie = () => {
    const memberInfo = getCookie("member")

    //닉네임 처리
    if (memberInfo && memberInfo.nickname) {
        memberInfo.nickname = decodeURIComponent(memberInfo.nickname)
    }
    return memberInfo
}

//Atom 생성시 초기값 지정 : Primitive atoms(write 가능)
const signinState = atom(initState||loadMemberCookie)

export default signinState;