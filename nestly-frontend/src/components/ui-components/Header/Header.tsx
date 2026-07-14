import { useNavigate } from "react-router-dom";
import { LogoName } from "../../../constants/constants";
import { PrimaryBtn } from "../Button/Button";
import styles from "./Header.module.scss";
import { useAppDispatch, useAppSelector } from "../../../redux/store/hooks";
import { logout } from "../../../redux/slices/userSlice";



const Header = () => {
  const navigate = useNavigate();
  const user = useAppSelector(state => state.authUser.user);
  const token = localStorage.getItem('token')
  const dispatch = useAppDispatch();


  return (
    <div className={styles.Header}>
        <div className={styles.LogoContainer}>
            <h2 onClick={()=>{
              navigate('/')
            }} className={styles.Logo}>{LogoName}</h2>
        </div>
        <div className={styles.LoginBtnContainer}>
           {token ?  <PrimaryBtn onClick={()=>{
              localStorage.removeItem('token')
              dispatch(logout())
              navigate('/')
            }}>Logout</PrimaryBtn> : 
             <PrimaryBtn onClick={()=>{
              navigate('/login')
            }}>Login</PrimaryBtn>}
        </div>

    </div>
  )
}

export default Header