import type { LoginData } from "../../pages/LoginPage/LoginPage.types";
import { apiSlice } from "./apiSlice";

export const authApi = apiSlice.injectEndpoints({
    endpoints: (build) => ({
        login : build.mutation({
            query: (data: LoginData) => ({
                url: '',
                body: data
            })
        })
    })     
})