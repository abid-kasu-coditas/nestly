import { createBrowserRouter } from "react-router-dom";
import Layout from "../pages/Layout/Layout";
import LandingPage from "../pages/LandingPage/LandingPage";

export const router = createBrowserRouter([
    {
        element:<Layout/>,
        path:'/',
        children: [
            {
                element: <LandingPage/>,
                index:true
            }
        ]
    }
])