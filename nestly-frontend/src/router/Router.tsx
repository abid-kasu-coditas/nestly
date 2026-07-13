import { createBrowserRouter } from "react-router-dom";
import Layout from "../pages/Layout/Layout";
import LandingPage from "../pages/LandingPage/LandingPage";
import LoginPage from "../pages/LoginPage/LoginPage";
import AuthGuard from "../components/ui-components/AuthGuard/AuthGuard";

export const router = createBrowserRouter([
    {
        element:<Layout/>,
        path:'/',
        children: [
            {
                element: <LandingPage/>,
                index:true
            },
            {
                element: <LoginPage/>,
                path: "/login",
                index:true
            },
            {
                element: <AuthGuard/>,
                children:[
                    {
                        
                    }
                ]
            }

        ]
    }
])