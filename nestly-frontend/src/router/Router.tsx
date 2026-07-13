import { createBrowserRouter } from "react-router-dom";
import Layout from "../pages/Layout/Layout";
import LandingPage from "../pages/LandingPage/LandingPage";
import LoginPage from "../pages/LoginPage/LoginPage";
import AuthGuard from "../components/ui-components/AuthGuard/AuthGuard";
import DashboardLayout from "../pages/DashboardLayout/DashboardLayout";
import DashboardRedirector from "../components/ui-components/DashboardRedirector/DashboardRedirector";

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
                        element:<DashboardRedirector/>,
                        path: "/dashboardRedirector"
                    },
                    {
                        element:<DashboardLayout/>,
                        path:"/properties"
                    }
                ]
            }

        ]
    }
])