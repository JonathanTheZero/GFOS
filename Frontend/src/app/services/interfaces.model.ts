namespace internal {
    export const apiUrl = "";
    export interface success {
        message: string;
    }

    export interface error {
        reason: string;
    }

    export function instanceOfSuccess(object: any): object is success {
        return 'message' in object;
    }

    export function instanceOfError(object: any): object is error {
        return 'reason' in object;
    }
}

namespace request {
    export interface success {
        message: string;
    }

    export interface error {
        reason: string;
    }

    export interface mitarbeiter {
        personalnummer: string;
        name: string;
        vorname: string;
        erreichbar: boolean;
        arbeitskonto: number;
        email: string | null;
        status: string | null;
        rechteklasse: string;
        abteilung: string | null;
        vertreter: string | null;
    }

    export function instanceOfSuccess(object: any): object is success {
        return 'message' in object;
    }

    export function instanceOfError(object: any): object is error {
        return 'reason' in object;
    }
}