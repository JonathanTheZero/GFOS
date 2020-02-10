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

namespace answer {
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