export interface User {
    id: string;
    role: string;
    email: string;
    displayName: string | null;
    contact: string | null;
    profilePictureUrl: string | null;
    verificationDocUrl: string | null;
    createdAt: string;
}


