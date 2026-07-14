import { Controller, useForm } from "react-hook-form"
import styles from "./LoginPage.module.scss"
import { PrimaryBtn } from "../../components/ui-components/Button/Button"
import type { LoginAction, LoginData, LoginState } from "./LoginPage.types";
import { useReducer } from "react";
import CheckCircleIcon from '@mui/icons-material/CheckCircleOutlined';
import { useNavigate } from "react-router-dom";
import { saveUser, userSlice } from "../../redux/slices/userSlice";
import { useDispatch } from "react-redux";
import { useFetchOTPMutation, useLoginMutation } from "../../redux/slices/authApiSlice";


const initialState = {
    otpVerified : false,
    otpSent: false,
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
    const navigate = useNavigate();
    const dispatchFn = useDispatch();
    const [ login ] = useLoginMutation();
    const {  handleSubmit, control,  formState: { isLoading }} = useForm<LoginData>();

    const [fetchOTPservice] = useFetchOTPMutation();
    const handleLogin = async(data: LoginData) => {
        
        if(state.otpSent && state.otpVerified){
            const user = {
                id: "1",
                role: "Admin",
                email: "admin@demo.io"
            }
            dispatchFn(saveUser(user))
            navigate('/dashboardRedirector')
        }
    }

    const fetchOTP = async(data: LoginData) => {
        console.log("OTP Requested", data)
        const response = await fetchOTPservice(data);
        console.log(response)
    }

    const verifyOTP = () => {
        setTimeout(()=>{
            dispatch({type:"setOtpVerified"})
        }, 1000)
    }   


  return (
    <div className={styles.LoginPage}>
        <div className={styles.LoginPageOverlayContainer}>
            <div className={styles.LoginImage}></div>
            <div className={styles.LoginBox}>
                <h2>Login</h2>
                <form className={styles.LoginForm} onSubmit={!state.otpSent ? handleSubmit(fetchOTP) : handleSubmit(handleLogin)}>
                    <div className={styles.FormInput}>
                        {state.otpSent ? <>
                            <Controller
                            name="otp"
                            key={'otp-field'}
                            control={control}
                            render={({field})=>{
                                return <input className={styles.Input} placeholder="1234" {...field}></input>
                            }}
                            />
                            {state.otpVerified ? <CheckCircleIcon color="success"/> : <PrimaryBtn> Verify OTP</PrimaryBtn>}
                        </> : <>
                            <Controller
                            name="email"
                            key={'email-field'}
                            control={control}
                            render={({field})=>{
                                return <input className={styles.Input} placeholder="you@example.com" { ...field }></input>
                            }}
                            />
                            <PrimaryBtn>Get OTP</PrimaryBtn>
                        </>}
                    </div>
                    {state.otpVerified ? <PrimaryBtn>{isLoading ? "Logging in" : "Login"}</PrimaryBtn> : <PrimaryBtn className={styles.DisabledBtn} disabled>Login</PrimaryBtn>}
                </form>
            </div>
        </div>
    </div>
  )
}

export default LoginPage