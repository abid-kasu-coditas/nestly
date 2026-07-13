export interface LoginData {
    email: string;
    otp: string
}

export interface LoginState {
    otpSent: boolean;
    otpVerified: boolean;
}

export type LoginAction = {
    type: string;
}