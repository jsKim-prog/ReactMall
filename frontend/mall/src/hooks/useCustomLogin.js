import { useDispatch, useSelector } from "react-redux";
import { createSearchParams, Navigate, useNavigate } from "react-router-dom";
import { loginPostAsync, logout } from "../slices/loginSlice";

const useCustomLogin = ()=>{
    const navigate = useNavigate()
    const dispatch = useDispatch()
    const loginState = useSelector(state=>state.loginSlice) //로그인 상태
    const isLogin = loginState.email? true : false //로그인 여부
    
    //로그인 함수
    const doLogin = async (loginParam) =>{
        const action = await dispatch(loginPostAsync(loginParam))
        return action.payload
    }

    //로그아웃 함수
    const doLogout = ()=>{
        dispatch(logout())
    }

    //페이지 이동
    const moveToPath = (path) =>{
        navigate({pathname:path}, {replace:true})
    }

    //로그인 페이지로 이동
    const moveToLogin = () =>{
        navigate({pathname:`/member/login`}, {replace:true})
    }

    //로그인 페이지로 이동 컴포넌트
    const moveToLoginReturn = ()=>{
        return <Navigate replace to="/member/login"></Navigate>
    }
    
    //Token 관련 예외처리
    const exceptionHandle = (ex) =>{
        console.log("Token Exception.....")
        console.log(ex)

        const errorMsg = ex.response.data.error
        const errorStr = createSearchParams({error:errorMsg}).toString()

        if(errorMsg==='REQUIRE_LOGIN'){ //ACCESS TOKEN이 없는 경우
            alert("로그인이 필요합니다.")
            navigate({pathname:`/member/login`, search:errorStr})
            return
        }

        if(ex.response.data.error === 'ERROR_ACCESSDENIED'){//권한이 없는 경우
            alert("해당 메뉴를 사용할 수 있는 권한이 없습니다.")
            navigate({pathname:`/member/login`, search:errorStr})
            return
        }
    }

    return {loginState, isLogin, doLogin, doLogout, moveToPath, moveToLogin, moveToLoginReturn, exceptionHandle}
}

export default useCustomLogin;