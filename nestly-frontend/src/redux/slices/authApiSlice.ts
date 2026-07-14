import type { LoginData } from "../../pages/LoginPage/LoginPage.types";
import { apiSlice } from "./apiSlice";

export const authApi = apiSlice.injectEndpoints({
    endpoints: (build) => ({
        login : build.mutation({
            query: (data: LoginData) => ({
                url: 'auth/verify-otp',
                body: data,
                method: "POST"
            })
        }),
        fetchOTP : build.mutation({
            query: (data: {email : string}) => ({
                url: 'auth/request-otp',
                body: data,
                method: "POST"
            })
        }),
        getMe : build.mutation({
            query: (token: string) => ({
                url: 'users/me',
                headers : {
                    'Authorization' : `Bearer ${token}`
                }
            })
        }),
        
        
    })     
})


export const { useLoginMutation, useFetchOTPMutation, useGetMeMutation } = authApi;