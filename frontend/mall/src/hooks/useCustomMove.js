import { useState } from "react";
import { createSearchParams, useNavigate, useSearchParams } from "react-router-dom";

/*useSearchParams()를 통해 받은 쿼리스트링을 처리하는 공통함수*/
const getNum=(param, defaultValue)=>{
    if(!param){
        return defaultValue;
    }
    return parseInt(param)
}

const useCustomMove = ()=>{
    const navigate = useNavigate()
    //같은 페이지 버튼 클릭 시에도 서버 호출위한 상태감지
    const [refresh, setRefresh] = useState(false)

    const [queryParams] = useSearchParams()
    const page = getNum(queryParams.get('page'), 1)
    const size = getNum(queryParams.get('size'), 10)
    const queryDefault = createSearchParams({page, size}).toString()

    const moveToList = (pageParam) =>{
        let queryStr=""
        if(pageParam){
            const pageNum = getNum(pageParam.page, 1)
            const sizeNum = getNum(pageParam.size, 10)

            queryStr = createSearchParams({page:pageNum, size:sizeNum}).toString()
        }else{
            queryStr = queryDefault
        }
        setRefresh(!refresh) //상태변화감지 추가
        navigate({
            pathname:`../list`,
            search:queryStr
        })
    }

    const moveToModify = (num) =>{
        console.log(queryDefault)
        navigate({
            pathname:`../modify/${num}`,
            search:queryDefault //수정시 기존의 쿼리스트링 유지
        })
    }

    const moveToRead = (num) =>{
        console.log(queryDefault)
        navigate({
            pathname:`../read/${num}`,
            search: queryDefault
        })
    }

    return {moveToList, moveToModify, moveToRead, page, size, refresh};
}

export default useCustomMove;