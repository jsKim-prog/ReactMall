import axios from "axios";
import { API_SERVER_HOST } from "./todoApi";

const host = `${API_SERVER_HOST}/api/products`

//상품등록 : post /
export const postAdd = async (product) =>{
    const header = {headers:{"Content-Type":"multipart/form-data"}}
    const res = await axios.post(`${host}/`, product, header)
    return res.data
}

//리스트 가져오기 : get /list
export const getList = async (pageParam) =>{
    const {page, size} = pageParam
    const res = await axios.get(`${host}/list`, {params:{page:page, size:size}})
    return res.data
}

//상품조회 : get /{pno}
export const getOne = async (tno) =>{
    const res = await axios.get(`${host}/${tno}`)
    return res.data
}

//상품정보 수정: put /{pno}
export const putOne = async (pno, product) =>{
    const header = {headers:{"Content-Type":"multipart/form-data"}}
    const res = await axios.put(`${host}/${pno}`, product, header)
    return res.data
}

//상품 삭제처리 : delete /{pno}
export const deleteOne = async (pno) =>{
    const res = await axios.delete(`${host}/${pno}`)
    return res.data
}