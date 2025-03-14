import { useEffect, useState } from "react";
import useCustomMove from "../../hooks/useCustomMove"
import { getList } from "../../api/productApi";
import FetchingModal from "../common/FetchingModal"
import { API_SERVER_HOST } from "../../api/todoApi";
import PageComponent from "../common/PageComponent"

const host = API_SERVER_HOST //이미지 경로용
const initState = {
    dtoList:[],
    pageNumList:[],
    pageRequestDTO:null,
    prev:false, 
    next:false,
    totalCount:0, 
    prevPage:0, 
    nextPage:0, 
    totalPage:0, 
    current:0
}

const ListComponent = ()=>{
    const {page, size, refresh, moveToList, moveToRead} = useCustomMove()
    const [serverData, setServerData] = useState(initState)
    //Modal
    const [fetching, setFetching] = useState(false)

    useEffect(()=>{
        setFetching(true)
        getList({page, size}).then(data=>{
            console.log(data)
            setServerData(data)
            setFetching(false)
        })
    },[page, size, refresh])

    return(
        <div className="border-2 border-blue-100 mt-10 mr-2 ml-2">
            {fetching? <FetchingModal/>:<></>}
            <div className="flex flex-wrap mx-auto p-6">
                {serverData.dtoList.map(product =>
                    <div key={product.pno} className="" onClick={()=>moveToRead(product.pno)}>
                        <div className="flex flex-col h-full">
                            <div className="font-extrabold text-2xl p-2 w-full">{product.pno}</div>
                            <div className="text-1xl m-1 p-2 w-full flex flex-col">
                                <div className="w-full overflow-hidden">
                                    <img alt="product" className="m-auto rounded-md w-60"
                                    src={`${host}/api/products/view/s_${product.uploadFileNames[0]}`}/>
                                </div>
                                <div className="bottom-0 font-extrabold bg-white">
                                    <div className="text-center p-1">이름 : {product.pname}</div>
                                    <div className="text-center p-1">가격 : {product.price}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        <PageComponent serverData={serverData} movePage={moveToList}/>    
        </div>
    );
}

export default ListComponent;