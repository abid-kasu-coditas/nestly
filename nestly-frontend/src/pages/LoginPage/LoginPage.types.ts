export interface LoginData {
    email: string;
    code: string
}

export interface LoginState {
    otpSent: boolean;
    otpVerified: boolean;
}

type LoginType = "setOtpSent" | "setOtpVerified" 

export type LoginAction = {
    type: LoginType;
}