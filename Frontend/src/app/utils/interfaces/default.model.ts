export interface Mitarbeiter {
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

export interface apiAnswer {
    fehler?: string;
    erfolg?: string;
}