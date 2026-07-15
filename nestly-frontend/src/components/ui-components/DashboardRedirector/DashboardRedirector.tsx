import { useEffect } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import { useAppSelector } from "../../../redux/store/hooks";


const DashboardRedirector = () => {
    
    const user  = useAppSelector((state)=> state.authUser.user);
    const navigate = useNavigate();
    const token = localStorage.getItem("token")
    if(!token){
        return
    }

    useEffect(()=>{
        switch(user?.role){
            case "ADMIN":
                console.log("admin")
                navigate('/manage')
                break;
            case "TENANT":
                navigate('/properties')
                break;
        }
    },[user])

    return <Outlet />
}

export default DashboardRedirector