export interface registerForm {
    name: string;
    lastName: string;
    password: string;
    email: string;
    district: string;
    accessLevel: "admin" | "personnelDepartment" | "headOfDepartment" | "user" | null;
    agent: string;
}

export interface errorObj {
    reason: string;
}