import { createBrowserRouter } from "react-router-dom";
import Layout from "../pages/Layout/Layout";
import LandingPage from "../pages/LandingPage/LandingPage";
import LoginPage from "../pages/LoginPage/LoginPage";
import AuthGuard from "../components/ui-components/AuthGuard/AuthGuard";
import DashboardLayout from "../pages/DashboardLayout/DashboardLayout";
import DashboardRedirector from "../components/ui-components/DashboardRedirector/DashboardRedirector";
import ManageUsers from "../pages/ManageUsers/ManageUsers";
import PropertyListing from "../pages/PropertyListing/PropertyListing";
import ProfilePage from "../pages/ProfilePage/ProfilePage";

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
                        index:true,
                        path: "/dashboardRedirector"
                    },
                    {
                        element:<DashboardLayout/>,
                        children:[
                            {
                                element:<ManageUsers/>,
                                path: "/manage"
                            },
                            {
                                element:<PropertyListing/>,
                                path: "/listings"
                            },
                            {
                                element:<ProfilePage/>,
                                path: "/profile"
                            },
                        ]
                    }
                ]
            }

        ]
    }
])