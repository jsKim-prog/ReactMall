import { useParams } from "react-router-dom";
import ReadComponent from "../../components/todo/ReadComponent";

const ReadPage = ()=>{
    const {tno} = useParams()
    /*기존 navigate 코드 -> useCustomMove로 이동*/
    
    return(
    <div className="font-extrabold w-full bg-white mt-6">       
        <div className="text-3xl ">
        Todo Read Page Component {tno}
        </div>
        <ReadComponent tno={tno}></ReadComponent>    
    </div>    
    );
}

export default ReadPage;