import { useEffect, useRef, useState } from "react";
import { deleteOne, getOne, putOne } from "../../api/productApi";
import FetchingModal from "../common/FetchingModal";
import { API_SERVER_HOST } from "../../api/todoApi";
import useCustomMove from "../../hooks/useCustomMove";
import ResultModal from "../common/ResultModal"
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

const initState = {
    pno: 0,
    pname: '',
    pdesc: '',
    price: 0,
    delFlag: false,
    uploadFileNames: []
}

const host = API_SERVER_HOST
//reactQuery 적용
const ModifyComponent = ({ pno }) => {
    const [product, setProduct] = useState(initState)
   
    const { moveToRead, moveToList } = useCustomMove()

    const uploadRef = useRef()
    const queryClient = useQueryClient()
    //수정처리-useMutation()
    const modMutation = useMutation({
        mutationFn:(product) => putOne(pno, product)
    })
    //삭제처리-useMutation()
    const mutationFn = (pno) => deleteOne(pno)
    const delMutation = useMutation({mutationFn})

    const query = useQuery({
        queryKey: ['products', pno],
        queryFn: () => getOne(pno),
        options:{
            staleTime:Infinity
        }
    })
    //useQuery()가 무한로딩을 하지 않기 위한 useEffect()
    useEffect(() => {
        if(query.isSuccess){
            setProduct(query.data)
        }
    }, [pno, query.data, query.isSuccess])

    //입력칸 정보변경 관리
    const handleChangeProduct = (e) => {
        product[e.target.name] = e.target.value
        setProduct({ ...product })
    }

    //기존 이미지 삭제 : uploadFileNames에서 이미지 삭제
    const deleteOldImages = (imageName) => {
        const resultFileNames = product.uploadFileNames.filter(fileName => fileName !== imageName)
        product.uploadFileNames = resultFileNames
        setProduct({ ...product })
        // imageName : Delete 버튼을 누른 이미지 파일명
        // resultFileNames :  Delete 버튼을 누르지 않은(삭제하지 않은) 이미지 파일명
    }

    // 수정 버튼 액션
    
    const handleClickModify = () => {
        const files = uploadRef.current.files
        const formData = new FormData()

        for (let i = 0; i < files.length; i++) {
            formData.append("files", files[i]);
        }

        formData.append("pname", product.pname)
        formData.append("pdesc", product.pdesc)
        formData.append("price", product.price)
        formData.append("delFlag", product.delFlag)

        for (let i = 0; i < product.uploadFileNames.length; i++) {
            formData.append("uploadFileNames", product.uploadFileNames[i])
        }
        
        //useQuery() 적용
        modMutation.mutate(formData)
    }

    //삭제버튼 액션
    const handleClickDelete = () => {
       //useQuery() 적용
       delMutation.mutate(pno)
    }

    //결과모달 닫기
    const closeModal = () => {
        //useQuery() 적용
        if(delMutation.isSuccess){
            queryClient.invalidateQueries({queryKey:['products', pno]})
            queryClient.invalidateQueries({queryKey:['products/list']})
            moveToList()
        }
        if(modMutation.isSuccess){
            queryClient.invalidateQueries({queryKey:['products', pno]})
            queryClient.invalidateQueries({queryKey:['products/list']})
            moveToRead(pno)
        }
    }

    return (
        <div className="border-2 border-sky-200 mt-2 m-2 p-4">
            {query.isFetching || delMutation.isLoading || modMutation.isLoading ? <FetchingModal /> : <></>}
            {delMutation.isSuccess || modMutation.isSuccess ?
                <ResultModal
                    title={'처리결과'}
                    content={'정상적으로 처리되었습니다.'}
                    callbackFn={closeModal} /> : <></>} 
            <div className="flex justify-center">
                <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div className="w-1/5 p-6 text-right font-bold">Product Name</div>
                    <input className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
                        name="pname"
                        type={'text'}
                        value={product.pname}
                        onChange={handleChangeProduct}></input>
                </div>
            </div>
            <div className="flex justify-center">
                <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div className="w-1/5 p-6 text-right font-bold">Desc.</div>
                    <textarea className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md resize-y"
                        name="pdesc"
                        rows="4"
                        value={product.pdesc}
                        onChange={handleChangeProduct}>{product.pdesc}</textarea>
                </div>
            </div>
            <div className="flex justify-center">
                <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div className="w-1/5 p-6 text-right font-bold">Price</div>
                    <input className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
                        name="price"
                        type={'number'}
                        value={product.price}
                        onChange={handleChangeProduct}></input>
                </div>
            </div>
            <div className="flex justify-center">
                <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div className="w-1/5 p-6 text-right font-bold">DELETE</div>
                    <select
                        name="delFlag" value={product.delFlag}
                        onChange={handleChangeProduct}
                        className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md">
                        <option value={false}>사용</option>
                        <option value={true}>삭제</option>
                    </select>
                </div>
            </div>
            <div className="flex justify-center">
                <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div className="w-1/5 p-6 text-right font-bold">Files</div>
                    <input ref={uploadRef}
                        type={'file'}
                        multiple={true}
                        className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"></input>
                </div>
            </div>
            <div className="flex justify-center">
                <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div className="w-1/5 p-6 text-right font-bold">Images</div>
                    <div className="w-4/5 justify-center flex flex-wrap items-start">
                        {product.uploadFileNames.map((imgFile, i) =>
                            <div className="flex justify-center flex-col w-1/3 m-1 align-baseline" key={i}>
                                <button className="bg-blue-500 text-1xl text-white" onClick={() => deleteOldImages(imgFile)}>DELETE</button>
                                <img alt="img" src={`${host}/api/products/view/s_${imgFile}`}></img>
                            </div>
                        )}
                    </div>
                </div>
            </div>
            <div className="flex justify-end p-4">
                <button type="button" className="rounded p-4 m-2 text-xl w-32 text-white bg-red-500" onClick={handleClickDelete}>Delete</button>
                <button type="button" className="inline-block rounded p-4 m-2 text-xl w-32 text-white bg-orange-500" onClick={handleClickModify}>Modify</button>
                <button type="button" className="rounded p-4 m-2 text-xl w-32 text-white bg-blue-500" onClick={moveToList}>List</button>
            </div>
        </div>
    );
}

export default ModifyComponent;