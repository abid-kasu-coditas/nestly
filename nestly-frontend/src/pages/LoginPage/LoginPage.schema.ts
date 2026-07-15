import {email, z} from "zod";

export const LoginSchema = z.object({
    email: z.email("Please Enter valid Email"),
    code: z.string("Please Enter OTP")
})

export type LoginFormData = z.infer<typeof LoginSchema>;