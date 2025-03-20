import axios from "axios"
import {API_SERVER_HOST} from "./todoApi"


const rest_api_key = process.env.REACT_APP_REST_API //client_id: 앱 REST API 키
const redirect_uri = process.env.REACT_APP_REDIRECT_URI //redirect_uri: 인가 코드를 전달받을 서비스 서버의 URI(로그인후 이동할 URI, 플랫폼에 등록한 Redirect uri)
const auth_code_path = `https://kauth.kakao.com/oauth/authorize`//인가코드 요청 url(get method)
const access_token_url = `https://kauth.kakao.com/oauth/token`//토큰요청 주소(POST)


//인가코드 요청 : Rest Key + redirect URI -> 링크반환
export const getKakaoLoginLink = () =>{
    const kakaoURL = `${auth_code_path}?client_id=${rest_api_key}&redirect_uri=${redirect_uri}&response_type=code`

    return kakaoURL
}

//AccessToken 받기 (인가코드로 POST 요청)
export const getAccessToken = async (authCode) => {
    const header = {
        headers:{
            "Content-Type": "application/x-www-form-urlencoded"
        }
    }
   
    const params = {
        grant_type:"authorization_code",//고정
        client_id:rest_api_key,
        redirect_uri:redirect_uri,
        code:authCode
    }

    const rest = await axios.post(access_token_url, params, header)
    const accessToken = rest.data.access_token
    return accessToken
    
}

//AccessToken으로 API server 호출(get:/api/member/kakao)->사용자 정보 받기
export const getMemberWithAccessToken = async (accessToken) =>{
    const res = await axios.get(`${API_SERVER_HOST}/api/member/kakao?accessToken=${accessToken}`)

    return res.data
} 
