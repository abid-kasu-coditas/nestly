import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import type { User }from "../../types/user.types";


interface authState {
    user: User | null
}

const initialState: authState = {
    user: null 
}

export const userSlice = createSlice({
    name: 'user',
    initialState,
    reducers: {
        saveUser: (state: authState, action : PayloadAction<User>) => {
            state.user = action.payload
        },
        logout: (state : authState) => {
            state.user = null
        }
    },
})


export const { saveUser, logout } = userSlice.actions;

export default userSlice.reducer