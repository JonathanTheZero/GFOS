export interface accountSettingsForm {
    label: string;
    type: "password" | "email";
    formControlName: "email" | "password" | "newPassword" | "confirmNewPassword"; 
    error: {
        required?: string | null | undefined;
        minLength?: string | null | undefined;
        email?: string | null | undefined;
    }
}

interface selectData {
    value: string | number;
    text: string;
}

export type options = selectData[];