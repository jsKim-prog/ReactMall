import jwtAxios from "../util/jwtUtil"

export const API_SERVER_HOST = 'http://localhost:8080'
const prefix = `${API_SERVER_HOST}/api/todo`
// 조회
export const getOne = async (tno) =>{
    const res = await jwtAxios.get(`${prefix}/${tno}`)
    return res.data
}
// 리스트조회
export const getList = async (pageParam) =>{
    const {page, size} = pageParam
    const res = await jwtAxios.get(`${prefix}/list`,{params:{page:page, size:size}})
    return res.data
}

//등록(post)
export const postAdd = async (todoObj) => {
    const res = await jwtAxios.post(`${prefix}/`, todoObj)
    return res.data
}

//수정(put)
export const putOne = async (todo) =>{
    const res = await jwtAxios.put(`${prefix}/${todo.tno}`, todo)
    return res.data
}

//삭제(delete)
export const deleteOne = async (tno) =>{
    const res = await jwtAxios.delete(`${prefix}/${tno}`)
    return res.data
}