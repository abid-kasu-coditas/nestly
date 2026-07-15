import { apiSlice } from "./apiSlice";
import type { AddUserData } from "../../components/ui-components/AddUserModal/AddUserModal.types";

export const adminApiSlice = apiSlice.injectEndpoints({
    endpoints: (build) => ({
            addUser : build.mutation({
                query: (data: AddUserData) => ({
                    url: 'admin/create-account',
                    body: data,
                    method: "POST",
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`
                    }
                })
            }),
             
        })     
})

export const { useAddUserMutation } = adminApiSlice;