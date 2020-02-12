namespace internal {
    export const apiUrl = "";
    export interface todo {
        title: string;
        subs?: todo[]
    }
}

namespace request {
    export interface authAnswer {
        message: string;
        error?: string;
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
}