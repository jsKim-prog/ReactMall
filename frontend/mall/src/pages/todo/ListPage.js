import ListComponent from "../../components/todo/ListComponent";

const ListPage=()=>{
    //쿼리스트링 받는 queryParams는 ListComponent, useCustomMove로 이동

    return(
    <div className="p-4 w-full bg-white">
        <div className="text-3xl font-extrabold">
            Todo List Page Component 
        </div>
        <ListComponent/>
    </div>
    );
}

export default ListPage;