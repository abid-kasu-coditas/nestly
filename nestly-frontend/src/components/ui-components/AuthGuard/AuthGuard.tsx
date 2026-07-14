import { Navigate, Outlet } from 'react-router-dom'
import { useGetMeMutation } from '../../../redux/slices/authApiSlice';
import { saveUser } from '../../../redux/slices/userSlice';
import { useEffect } from 'react';
import { useAppSelector } from '../../../redux/store/hooks';

const AuthGuard = () => {

    const token = localStorage.getItem("token")
    const [getMeService] = useGetMeMutation();
    const user = useAppSelector(state => state.authUser.user)

    const fetchMe = async() => {
      const response = await getMeService(localStorage.getItem('token') as string).unwrap();
      if(response.id){
        saveUser(response)
        console.log(response)
      }

    }
    if(!token) {
        return <Navigate to="/login"/>
    }

    useEffect(()=>{
      console.log("Authguard");
      fetchMe()
    },[])


  return (
    <Outlet />
  )
}

export default AuthGuard