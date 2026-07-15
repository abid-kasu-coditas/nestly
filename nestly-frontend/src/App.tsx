import { RouterProvider } from "react-router-dom";
import { router } from "./router/Router";
import { ToastContainer } from "react-toastify";


const App = () => {
  return <>
    <RouterProvider router={router}/>
    <ToastContainer position="top-center"/>
  </>
}

export default App