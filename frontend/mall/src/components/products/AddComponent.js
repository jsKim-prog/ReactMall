import { useRef, useState } from "react";
import { postAdd } from "../../api/productApi";
import FetchingModal from "../common/FetchingModal";
import ResultModal from "../common/ResultModal";
import useCustomMove from "../../hooks/useCustomMove";
import { useMutation, useQueryClient } from "@tanstack/react-query";

const initState = {
    pname:'',   
    pdesc:'',
    price:0,
    files:[]
}
//useMutation() 적용 수정
const AddComponent = () =>{
    const [product, setProduct] = useState({...initState})
    //경로관리
    const uploadRef = useRef()
    //완료 후 이동
    const {moveToList} = useCustomMove()

    //입력창 변경 관리
    const handleChangeProduct = (e) =>{
        product[e.target.name] = e.target.value
        setProduct({...product})
    }

    //useMutation()
    const mutationFn = (product)=> postAdd(product)
    const addMutation = useMutation({mutationFn})

    //등록버튼 클릭 액션
    const handleClickAdd = (e) =>{
        console.log("clickAdd:"+product)
        //첨부파일의 정보 파악
        const files = uploadRef.current.files
        //FormData 전송용 객체로 구성
        const formData = new FormData()

        for(let i=0; i<files.length; i++){
            formData.append("files", files[i]);
        }

        formData.append("pname", product.pname)
        formData.append("pdesc", product.pdesc)
        formData.append("price", product.price)

        addMutation.mutate(formData) //useMutation()
        console.log("useMutation...")
    }

    //결과모달 닫기
    // 모달창 닫을 때는 reactQuery 초기화
    const queryClient = useQueryClient()
    const closeModal = ()=>{
        queryClient.invalidateQueries("products/list")
        moveToList({page:1})
    }

    return(
        <div className="border-2 border-sky-200 mt-10 m-2 p-4">
            {addMutation.isLoading? <FetchingModal/>:<></>}
            {addMutation.isSuccess?
            <ResultModal
            title={'Product Add Result'}
            content={`Add Success ${addMutation.data.result}`}
            callbackFn={closeModal}/>:<></>} 
            <div className="flex justify-center">
                <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div className="w-1/5 p-6 text-right font-bold">Product Name</div>
                    <input className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
                    name="pname"
                    type={'text'}
                    value={product.pname}
                    onChange={handleChangeProduct}>                        
                    </input>
                </div>
            </div>
            <div className="flex justify-center">
                <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div className="w-1/5 p-6 text-right font-bold">Desc.</div>
                    <textarea className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md resize-y"
                    name="pdesc"
                    rows="4"
                    value={product.pdesc}
                    onChange={handleChangeProduct}> {product.pdesc}                       
                    </textarea>
                </div>
            </div>    
            <div className="flex justify-center">
                <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div className="w-1/5 p-6 text-right font-bold">Price</div>
                    <input className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
                    name="price"
                    type={'number'}
                    value={product.price}
                    onChange={handleChangeProduct}>                        
                    </input>
                </div>
            </div> 
            {/*파일입력*/}
            <div className="flex justify-center">
                <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div className="w-1/5 p-6 text-right font-bold">files</div>
                    <input ref={uploadRef} className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md" 
                    type={'file'}
                    multiple={true}></input>
                </div>
            </div> 
            {/*버튼*/}
            <div className="flex justify-end">
                <div className="relative mb-4 p-4 flex-wrap items-stretch">
                    <button type="button" className="rounded p-4 w-36 bg-blue-500 text-xl text-white" onClick={handleClickAdd}>ADD</button>
                </div>
            </div>
        </div>

    );
}

export default AddComponent;