export interface LoginData {
    email: string;
    otp: string
}

export interface LoginState {
    otpSent: boolean;
    otpVerified: boolean;
}

type LoginType = "setOtpSent" | "setOtpVerified" 

export type LoginAction = {
    type: LoginType;
}