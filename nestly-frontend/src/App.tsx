import styles from "./App.module.scss";
import Header from "./components/ui-components/Header/Header";
import LandingPage from "./pages/LandingPage/LandingPage";

const App = () => {
  return (
    <div className={styles.App}>
      <Header/>
      <LandingPage/>
    </div>
  )
}

export default App