export interface registerForm {
    name: string;
    lastName: string;
    password: string;
    email: string;
    district: string;
    accessLevel: "admin" | "personalabteilung" | "gruppenleiter" | "user" | null;
    agent: string;
}

export interface errorObj {
    reason: string;
}