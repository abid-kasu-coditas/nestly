import { useEffect } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import { useAppSelector } from "../../../redux/store/hooks";


const DashboardRedirector = () => {
    
    const user  = useAppSelector((state)=> state.authUser.user);
    const navigate = useNavigate();
    console.log("redirector:", user)
    const token = localStorage.getItem("token")
    if(!token){
        return
    }

    useEffect(()=>{
        console.log("RUnning redirector")
        switch(user?.role){
            case "ADMIN":
                console.log("admin")
                navigate('/manage')
                break;
            case "USER":
                navigate('/properties')
                break;
        }
    },[user])

    return <Outlet />
}

export default DashboardRedirector