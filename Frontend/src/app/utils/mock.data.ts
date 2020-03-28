import { Mitarbeiter, Arbeitsgruppe } from './interfaces/default.model';

export const employeeSamples: Mitarbeiter[] = [
    {
        personalnummer: "000000000001",
        name: "Somafehlt",
        vorname: "Nils",
        erreichbar: false,
        arbeitskonto: 0,
        email: "nils.sommerfeld@gmx.net",
        status: "abwesend",
        rechteklasse: "root",
        abteilung: "IT-Sicherheit",
        vertreter: "000000000002",
        grundDAbw: "-"
    },
    {
        personalnummer: "01",
        name: "Test",
        vorname: "Auch",
        erreichbar: true,
        arbeitskonto: 0,
        email: "x@example.org",
        status: "string",
        rechteklasse: "root",
        abteilung: "string",
        vertreter: "string",
        grundDAbw: "-"
    },
    {
        personalnummer: "02",
        name: "Hmmmm",
        vorname: "Müller",
        erreichbar: true,
        arbeitskonto: 0,
        email: "x@example.org",
        status: "string",
        rechteklasse: "user",
        abteilung: "string",
        vertreter: "string",
        grundDAbw: "-"
    },
    {
        personalnummer: "02",
        name: "Hmmmm",
        vorname: "Müller",
        erreichbar: true,
        arbeitskonto: 0,
        email: "x@example.org",
        status: "string",
        rechteklasse: "user",
        abteilung: "string",
        vertreter: "string",
        grundDAbw: "-"
    },
    {
        personalnummer: "02",
        name: "Hmmmm",
        vorname: "Müller",
        erreichbar: true,
        arbeitskonto: 0,
        email: "x@example.org",
        status: "string",
        rechteklasse: "user",
        abteilung: "string",
        vertreter: "string",
        grundDAbw: "-"
    },
    {
        personalnummer: "02",
        name: "Hmmmm",
        vorname: "Müller",
        erreichbar: false,
        arbeitskonto: 0,
        email: "x@example.org",
        status: "string",
        rechteklasse: "user",
        abteilung: "string",
        vertreter: "string",
        grundDAbw: "Hab zu tun"
    }
];

export const groupSamples: Arbeitsgruppe[] = [
    {
        mitglieder: [
            "00000000001",
            "00000000002",
            "00000000001",
            "00000000001",
            "00000000001"
        ],
        leiter: "00000000001",
        bezeichnung: "Coole Gruppe",
        arbeitsgruppenID: "5"
    },
    {
        mitglieder: [
            "00000000001",
            "00000000002",
            "00000000001",
            "00000000002",
            "00000000001",
            "00000000001",
            "00000000001"
        ],
        leiter: "00000000001",
        bezeichnung: "Coole Gruppe2",
        arbeitsgruppenID: "53123"
    },
    {
        mitglieder: [
            "00000000001",
            "00000000002",
        ],
        leiter: "00000000001",
        bezeichnung: "Coole Gruppe324",
        arbeitsgruppenID: "512"
    }
]