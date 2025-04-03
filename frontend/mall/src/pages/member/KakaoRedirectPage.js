import { useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { getAccessToken, getMemberWithAccessToken } from "../../api/kakaoApi";
import useCustomLogin from "../../hooks/useCustomLogin"

const KakaoRedirectPage = () => {
    //url로 전달된 인가코드 받기
    const [searchParams] = useSearchParams()
    const authCode = searchParams.get("code")
    //API 서버 전송결과 받아 배포 -> recoil 적용으로 삭제,     
    const { moveToPath, saveAsCookie } = useCustomLogin()

    //인가코드 변경시 getAccessToken() 호출
    useEffect(() => {
        getAccessToken(authCode).then(accessToken => {
            console.log(accessToken)
            //사용자 정보 받기
            getMemberWithAccessToken(accessToken).then(memberInfo => {
                console.log("kakao member info from API server.............")
                console.log(memberInfo)
                //recoil 적용
                saveAsCookie(memberInfo)

                //소셜회원이 아니라면
                if (memberInfo && !memberInfo.social) {
                    moveToPath("/")
                } else {
                    moveToPath("/member/modify")
                }
            })
        })
    }, [authCode])
    return (
        <div>
            <div>Kakao Login Redirect</div>
            <div>{authCode}</div>
        </div>
    );
}

export default KakaoRedirectPage;