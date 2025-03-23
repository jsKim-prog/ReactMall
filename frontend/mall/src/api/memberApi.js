import axios from "axios"
import {API_SERVER_HOST} from "./todoApi"
//import jwtAxios from "../util/jwtUtil"

const host = `${API_SERVER_HOST}/api/member`

//login
export const loginPost = async (loginParam) =>{
    const header = {headers:{"Content-Type":"x-www-form-urlencoded"}}

    const form = new FormData()
    form.append("username", loginParam.email)
    form.append("password", loginParam.pw)

    const res = await axios.post(`${host}/login`, form, header)
    return res.data
}

//회원정보 수정(put : /api/member/modify)
export const modifyMember = async (member)=>{
    const res = await axios.put(`${host}/modify`, member)
    return res.data
}