
import { createSearchParams, Navigate, useNavigate } from "react-router-dom";
import signinState from "../atoms/signinState"
import { useRecoilState, useResetRecoilState } from "recoil";
import { loginPost } from "../api/memberApi";
import { removeCookie, setCookie } from "../util/cookieUtil";
import { cartState } from "../atoms/cartState"

//useRecoilState() 적용
const useCustomLogin = () => {
    const navigate = useNavigate()
    //useRecoilState() 적용
    const [loginState, setLoginState] = useRecoilState(signinState)
    const resetState = useResetRecoilState(signinState) //로그아웃 처리
    const resetCartState = useResetRecoilState(cartState) //장바구니 비우기

    const isLogin = loginState.email ? true : false //로그인 여부

    //로그인 함수 : useRecoilState() 적용
    const doLogin = async (loginParam) => {
        const result = await loginPost(loginParam)
        console.log(result)
        saveAsCookie(result)
        return result
    }

    //쿠키저장 :useRecoilState() 적용
    const saveAsCookie = (data) => {
        setCookie("member", JSON.stringify(data), 1) //1일
        setLoginState(data)
    }

    //로그아웃 함수 :useRecoilState() 적용
    const doLogout = () => {
        removeCookie('member')
        resetState()
        resetCartState() //장바구니 리코일 데이터 비우기
    }

    //페이지 이동
    const moveToPath = (path) => {
        navigate({ pathname: path }, { replace: true })
    }

    //로그인 페이지로 이동
    const moveToLogin = () => {
        navigate({ pathname: `/member/login` }, { replace: true })
    }

    //로그인 페이지로 이동 컴포넌트
    const moveToLoginReturn = () => {
        return <Navigate replace to="/member/login"></Navigate>
    }

    //Token 관련 예외처리
    const exceptionHandle = (ex) => {
        console.log("Token Exception.....")
        console.log(ex)

        const errorMsg = ex.response.data.error
        const errorStr = createSearchParams({ error: errorMsg }).toString()

        if (errorMsg === 'ERROR_LOGIN') { //ACCESS TOKEN이 없는 경우
            alert("로그인이 필요합니다.")
            navigate({ pathname: `/member/login`, search: errorStr })
            return
        }

        if (ex.response.data.error === 'ERROR_ACCESSDENIED') {//권한이 없는 경우
            alert("해당 메뉴를 사용할 수 있는 권한이 없습니다.")
            navigate({ pathname: `/member/login`, search: errorStr })
            return
        }
    }

    return { loginState, isLogin, doLogin, doLogout, saveAsCookie, moveToPath, moveToLogin, moveToLoginReturn, exceptionHandle }
}

export default useCustomLogin;