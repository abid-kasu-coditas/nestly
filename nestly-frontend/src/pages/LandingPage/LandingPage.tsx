import { useEffect } from "react";
import { Content_Text, LogoName } from "../../constants/constants";
import styles from "./LandingPage.module.scss";
import { useAppSelector } from "../../redux/store/hooks";
import { useNavigate } from "react-router-dom";


const LandingPage = () => {
  const user = useAppSelector(state => state.authUser.user)
  const token = localStorage.getItem("token")
  const navigate = useNavigate();

  useEffect(()=>{
    if(token || user){
      navigate('/dashboardRedirector')    
    }
  },[])

  return (
    <div className={styles.LandingPage}>
        <div className={styles.LandingPageOverlayContainer}>
            <div className={styles.TextContainer}>
              <div className={styles.TextHeadingContainer}>
                <h2 className={styles.TextHeading}>{LogoName}</h2>
                <p className={styles.SubHeading}>{Content_Text}</p>
              </div>
              <div className={styles.TextContent}>
                <p>
                  Lorem ipsum dolor sit amet consectetur adipisicing elit. Nisi suscipit cum quasi non dolor veritatis, dolores culpa nihil similique iste blanditiis, nemo fuga eos reiciendis cupiditate beatae doloribus facere architecto.
                  Ipsam labore vel tempore ea quae rerum quam dignissimos molestias, consequatur, iure non tempora delectus? Quisquam explicabo adipisci quos itaque temporibus quaerat dolor aspernatur repellat quo animi, quia error totam?
                  Id voluptatum provident, corrupti dicta fuga quis earum pariatur quaerat debitis autem accusantium expedita, ratione facilis odio, animi quidem sunt sapiente deserunt doloribus? Fugit non, consequatur unde labore ab cumque.
                </p>
              </div>
            </div>
        </div>
    </div>
  )
}

export default LandingPage