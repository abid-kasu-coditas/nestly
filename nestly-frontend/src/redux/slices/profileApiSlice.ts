import { apiSlice } from "./apiSlice";

export const profileApiSlice = apiSlice.injectEndpoints({
    endpoints: (build) => ({
            updateProfile : build.mutation({
                query: (data: any) => ({
                    url: '/users/upload-profile-photo',
                    body: data,
                    method: "POST",
                    headers: {
                        'Content-Type': 'multipart/form-data',
                        "token" : `Bearer ${localStorage.getItem('token')}`
                    },
                })
            }),
             
        })     
})

export const { useUpdateProfileMutation } = profileApiSlice;