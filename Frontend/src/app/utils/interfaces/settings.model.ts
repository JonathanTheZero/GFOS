export interface accountSettingsForm {
    label: string;
    type: "password" | "email";
    error: {
        message: string;
        type: "required" | null;
    }
}

interface selectData {
    value: string | number;
    text: string;
}

export type options = selectData[];