import { useNavigate } from "react-router-dom";
import { LogoName } from "../../../constants/constants";
import { PrimaryBtn } from "../Button/Button";
import styles from "./Header.module.scss";


const Header = () => {
  const navigate = useNavigate();
  

  return (
    <div className={styles.Header}>
        <div className={styles.LogoContainer}>
            <h2 onClick={()=>{
              navigate('/')
            }} className={styles.Logo}>{LogoName}</h2>
        </div>
        <div className={styles.LoginBtnContainer}>
            <PrimaryBtn onClick={()=>{
              navigate('/login')
            }}>Login</PrimaryBtn>
        </div>

    </div>
  )
}

export default Header