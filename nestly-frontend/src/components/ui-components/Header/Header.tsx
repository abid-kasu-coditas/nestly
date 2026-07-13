import { LogoName } from "../../../constants/constants";
import { PrimaryBtn } from "../Button/Button";
import styles from "./Header.module.scss";


const Header = () => {
  return (
    <div className={styles.Header}>
        <div className={styles.LogoContainer}>
            <h2 className={styles.Logo}>{LogoName}</h2>
        </div>
        <div className={styles.LoginBtnContainer}>
            <PrimaryBtn>Login</PrimaryBtn>
        </div>

    </div>
  )
}

export default Header