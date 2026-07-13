import { useNavigate } from "react-router-dom";

const DashboardRedirector = () => {
    const user = localStorage.getItem('token');
    const navigate = useNavigate();

    if(!user){
        navigate('/')
    }

  return (
    <div>
        Sucess!
    </div>
  )
}

export default DashboardRedirector