import { configureStore } from "@reduxjs/toolkit";
import { userSlice } from "../slices/userSlice";
import { apiSlice } from "../slices/apiSlice";

const store = configureStore({
    reducer: {
        [ apiSlice.reducerPath ] : apiSlice.reducer,
        authUser: userSlice.reducer,
    },
    middleware: (getDefaultMiddleware) => 
        getDefaultMiddleware().concat(apiSlice.middleware),
    devTools:true
})


export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch

export default store;