import { Controller, useForm } from "react-hook-form";
import styles from "./LoginPage.module.scss";
import { PrimaryBtn } from "../../components/ui-components/Button/Button";
import type { LoginAction, LoginData, LoginState } from "./LoginPage.types";
import { useReducer } from "react";
import CheckCircleIcon from "@mui/icons-material/CheckCircleOutlined";
import { useNavigate } from "react-router-dom";
import { saveUser, userSlice } from "../../redux/slices/userSlice";
import { useDispatch } from "react-redux";
import {
  useFetchOTPMutation,
  useLoginMutation,
} from "../../redux/slices/authApiSlice";

const initialState = {
  otpVerified: false,
  otpSent: false,
};

const reducer = (data: LoginState, action: LoginAction) => {
  switch (action.type) {
    case "setOtpSent":
      return {
        ...data,
        otpSent: true,
      };
    case "setOtpVerified":
      return {
        ...data,
        otpVerified: true,
      };
    default:
      return data;
  }
};

const LoginPage = () => {
  const [state, dispatch] = useReducer(reducer, initialState);
  const navigate = useNavigate();
  const dispatchFn = useDispatch();
  const [login] = useLoginMutation();
  const {
    handleSubmit,
    control,
    formState: { isLoading },
  } = useForm<LoginData>();

  const [fetchOTPservice] = useFetchOTPMutation();


  const handleLogin = async (data: LoginData) => {
    const response = await login(data).unwrap();
    if(response.token){
        localStorage.setItem('token', response.token)
        navigate('/dashboardRedirector')
    }
  };

  const fetchOTP = async (data: LoginData) => {
    const emailData = { email : data.email}
    const response = await fetchOTPservice(emailData);
    if(response.data){
        dispatch({type: "setOtpSent"})
    }
  };


  return (
    <div className={styles.LoginPage}>
      <div className={styles.LoginPageOverlayContainer}>
        <div className={styles.LoginImage}></div>
        <div className={styles.LoginBox}>
          <h2>Welcome Back!</h2>
          <form
            className={styles.LoginForm}
            onSubmit={
              !state.otpSent
                ? handleSubmit(fetchOTP)
                : handleSubmit(handleLogin)
            }
          >
            <div className={styles.FormInput}>
              {state.otpSent ? (
                <>
                  <Controller
                    name="code"
                    key={"otp-field"}
                    control={control}
                    render={({ field }) => {
                      return (
                        <input
                          className={styles.Input}
                          placeholder="1234"
                          {...field}
                        ></input>
                      );
                    }}
                  />
                  {state.otpVerified ? (
                    <CheckCircleIcon color="success" />
                  ) : (
                    <PrimaryBtn> Verify OTP</PrimaryBtn>
                  )}
                </>
              ) : (
                <>
                  <Controller
                    name="email"
                    key={"email-field"}
                    control={control}
                    render={({ field }) => {
                      return (
                        <input
                          className={styles.Input}
                          placeholder="you@example.com"
                          {...field}
                        ></input>
                      );
                    }}
                  />
                  <PrimaryBtn>Get OTP</PrimaryBtn>
                </>
              )}
            </div>
            {state.otpVerified ? (
              <PrimaryBtn>{isLoading ? "Logging in" : "Login"}</PrimaryBtn>
            ) : (
              ""
            )}
          </form>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
