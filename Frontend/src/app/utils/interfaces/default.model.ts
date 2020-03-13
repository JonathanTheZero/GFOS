export interface Mitarbeiter {
    personalnummer: string;
    name: string;
    vorname: string;
    erreichbar: boolean;
    arbeitskonto: number;
    email: string | null;
    status: string | null;
    rechteklasse: "admin" | "personalabteilung" | "gruppenleiter" | "user" | "root";
    abteilung: string | null;
    vertreter: string | null;
}

export interface apiAnswer {
    fehler?: string;
    erfolg?: string;
    data?: Mitarbeiter;
}

export interface apiStats {
    
}