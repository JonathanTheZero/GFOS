export interface accountSettingsForm {
    label: string;
    type: "password" | "email";
    error: {
        message: string;
        type: "required" | null;
    }
}