import { Controller, useForm } from "react-hook-form"
import styles from "./LoginPage.module.scss"
import { PrimaryBtn } from "../../components/ui-components/Button/Button"
import type { LoginAction, LoginData, LoginState } from "./LoginPage.types";
import { useReducer } from "react";


const initialState = {
    otpVerified : false,
    otpSent: true,
}

const reducer = (data: LoginState, action: LoginAction) => {
    switch(action.type){
        case "setOtpSent":
            return {
                ...data, otpSent: true
            }
        case "setOtpVerified":
            return {
                ...data, otpVerified: true
            }
        default:
            return data
    }
}

const LoginPage = () => {

    const [state, dispatch] = useReducer(reducer, initialState);

    const {  handleSubmit, control} = useForm<LoginData>();
    const handleLogin = (data: LoginData) => {
        console.log(data)
    }


  return (
    <div className={styles.LoginPage}>
        <div className={styles.LoginPageOverlayContainer}>
            <div className={styles.LoginImage}></div>
            <div className={styles.LoginBox}>
                <h2>Login</h2>
                <form className={styles.LoginForm} onSubmit={handleSubmit(handleLogin)}>
                    <div className={styles.EmailInput}>
                        {state.otpSent ? <>
                            <Controller
                            name="otp"
                            control={control}
                            render={({field})=>{
                                return <input className={styles.Input} placeholder="1234" {...field}></input>
                            }}
                            />
                            {state.otpVerified ? }
                        </> : <>
                            <Controller
                            name="email"
                            control={control}
                            render={({field})=>{
                                return <input className={styles.Input} placeholder="you@example.com" {...field}></input>
                            }}
                            />
                            <PrimaryBtn>Get OTP</PrimaryBtn>
                        </>}
                    </div>
                    {state.otpVerified ? <PrimaryBtn>Submit</PrimaryBtn> : <PrimaryBtn className={styles.DisabledBtn} disabled>Submit</PrimaryBtn>}
                </form>
            </div>
        </div>
    </div>
  )
}

export default LoginPage