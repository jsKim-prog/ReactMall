import { useParams } from "react-router-dom";
import ModifyComponent from "../../components/todo/ModifyComponent";
// 매개변수 {tno} : component로 이동
const ModifyPage = ()=>{
    // navigate : useCustomMove로 이동
    const {tno} = useParams()   //component 전달위한 추가
    return(
        <div className="p-4 w-full bg-white">
            <div className="text-3xl font-extrabold">
            Todo Modify Page
            </div>
            <ModifyComponent tno={tno}/>
        </div>        
    );
}

export default ModifyPage;