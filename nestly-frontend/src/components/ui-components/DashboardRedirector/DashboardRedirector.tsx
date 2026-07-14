import { useEffect } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import { useAppSelector } from "../../../redux/store/hooks";

const DashboardRedirector = () => {
    
    const user  = useAppSelector((state)=> state.authUser.user);
    const navigate = useNavigate();
    console.log(user)

    if(!user){
        navigate('/')
    }

    useEffect(()=>{
        switch(user?.role){
            case "Admin":
                navigate('/manage')
                break;
            case "User":
                navigate('/properties')
                break;
        }
    },[user])

    return <Outlet />
}

export default DashboardRedirector