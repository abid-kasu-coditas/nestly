import { RouterProvider } from "react-router-dom";
import styles from "./App.module.scss";
import { router } from "./router/Router";


const App = () => {
  return <>
    <RouterProvider router={router}/>
  </>
}

export default App