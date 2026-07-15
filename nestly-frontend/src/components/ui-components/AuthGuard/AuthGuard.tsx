import { Navigate, Outlet } from 'react-router-dom'
import { useGetMeMutation } from '../../../redux/slices/authApiSlice';
import { saveUser } from '../../../redux/slices/userSlice';
import { useEffect } from 'react';
import { useAppDispatch, useAppSelector } from '../../../redux/store/hooks';

const AuthGuard = () => {

    const token = localStorage.getItem("token")
    const [getMeService] = useGetMeMutation();
    const user = useAppSelector(state => state.authUser.user)
    const dispatch = useAppDispatch();

    const fetchMe = async() => {
      const response = await getMeService(localStorage.getItem('token') as string).unwrap();
      if(response.id){
        dispatch(saveUser(response))
      }
      console.log(response)

    }
    
    useEffect(()=>{
      fetchMe()
    },[])
    
    if(!token) {
        return <Navigate to="/login"/>
    }

  return (
    <Outlet />
  )
}

export default AuthGuard