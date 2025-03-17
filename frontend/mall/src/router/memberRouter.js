import { lazy, Suspense } from "react";

const Loading = <div>Loading....</div>
const LoginPage  = lazy(()=> import("../pages/member/LoginPage"))
const LogoutPage = lazy(() =>import("../pages/member/LogoutPage"))

const memberRouter = () =>{
    return[
        {
            path:"login",
            element:<Suspense fallback={Loading}><LoginPage/></Suspense>
        },
        {
            path:"logout",
            element:<Suspense fallback={Loading}><LogoutPage/></Suspense>
        }

    ]
}

export default memberRouter;