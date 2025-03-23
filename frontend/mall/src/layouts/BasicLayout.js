import BasicMenu from "../components/menu/BasicMenu";
import CartComponent from "../components/menu/CartComponent";

const BasicLayout = ({children}) => {
    return(
        <>
        {/*기존헤더대신 BasicMenu */}
        <BasicMenu></BasicMenu>

        <div className="bg-white w-full flex flex-col space-y-4 md:flex-row md:space-x-4 md:space-y-0">
            <main className="bg-sky-300 md:w-2/3 lg:w-3/4 px-5 py-5">{children}</main>
            <aside className="bg-green-300 md:w-1/3 lg:w-1/4 px-5 flex py-5">
            <CartComponent/>
            </aside>
        </div>
        </>

    );
}

export default BasicLayout;