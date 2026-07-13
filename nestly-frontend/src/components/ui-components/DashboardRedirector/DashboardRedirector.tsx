import { useEffect } from "react";
import { Outlet, useNavigate } from "react-router-dom";

const DashboardRedirector = () => {
    const user = localStorage.getItem('token');
    const navigate = useNavigate();

    if(!user){
        navigate('/')
    }

    useEffect(()=>{
        navigate('/properties')
    },[])

    return <Outlet />
}

export default DashboardRedirector