import { Navigate, Outlet } from 'react-router-dom'
import { useGetMeMutation } from '../../../redux/slices/authApiSlice';

const AuthGuard = () => {

    const token = localStorage.getItem("token")
    const [getMeService] = useGetMeMutation();

    const fetchMe = async() => {
      const response = await getMeService(token as string).unwrap();
      if(response.status){

      }

    }
    if(!token) {
        return <Navigate to="/"/>
    }
  return (
    <Outlet />
  )
}

export default AuthGuard