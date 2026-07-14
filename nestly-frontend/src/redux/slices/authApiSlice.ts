import type { LoginData } from "../../pages/LoginPage/LoginPage.types";
import { apiSlice } from "./apiSlice";

export const authApi = apiSlice.injectEndpoints({
    endpoints: (build) => ({
        login : build.mutation({
            query: (data: LoginData) => ({
                url: 'auth/request-otp',
                body: data
            })
        }),
        fetchOTP : build.mutation({
            query: (data: Omit<LoginData,"otp">) => ({
                url: 'auth/request-otp',
                body: data.email,
                method: "POST"
            })
        })
    })     
})


export const { useLoginMutation, useFetchOTPMutation } = authApi;