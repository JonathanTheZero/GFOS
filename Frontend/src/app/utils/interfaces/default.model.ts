export interface Mitarbeiter {
    personalnummer: string;
    name: string;
    vorname: string;
    erreichbar: boolean;
    arbeitskonto: number;
    email: string | null;
    status: string | null;
    rechteklasse: "admin" | "personnelDepartment" | "headOfDepartment" | "user" | "root";
    abteilung: string | null;
    vertreter: string | null;
    passwort?: string | null;
    grundDAbw: string;
}

export type MitarbeiterAtribut = "personalnummer" | "name" | "vorname" | "erreichbar" | "arbeitskonto" | "email" | "status" | "rechteklasse" | "abteilung" | "vertreter" | "grundDAbw";

export interface apiAnswer {
    fehler?: string;
    erfolg?: string;
    data?: Mitarbeiter | Arbeitsgruppe;
}

export interface Arbeitsgruppe {
    mitglieder: Array<string>;
    leiter: string;
    bezeichnung: string;
    arbeitsgruppenID: string;
}