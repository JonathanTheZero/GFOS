import { Todo } from './interfaces/dashboard.model';
import { Mitarbeiter } from './interfaces/default.model';

export const todoSamples: Todo[] = [
    {
        title: "Test"
    },
    {
        title: "Test2"
    },
    {
        title: "Test3",
        subs: [
            {
                title: "Test4"
            },
            {
                title: "Test5"
            }
        ]
    }
]

export const employeeSamples: Mitarbeiter[] = [
    {
        personalnummer: "01",
        name: "Test",
        vorname: "Auch",
        erreichbar: true,
        arbeitskonto: 0,
        email: "x@example.org",
        status: "string",
        rechteklasse: "string",
        abteilung: "string",
        vertreter: "string"
    },
    {
        personalnummer: "02",
        name: "Hmmmm",
        vorname: "Müller",
        erreichbar: true,
        arbeitskonto: 0,
        email: "x@example.org",
        status: "string",
        rechteklasse: "string",
        abteilung: "string",
        vertreter: "string"
    },
    {
        personalnummer: "02",
        name: "Hmmmm",
        vorname: "Müller",
        erreichbar: true,
        arbeitskonto: 0,
        email: "x@example.org",
        status: "string",
        rechteklasse: "string",
        abteilung: "string",
        vertreter: "string"
    },
    {
        personalnummer: "02",
        name: "Hmmmm",
        vorname: "Müller",
        erreichbar: true,
        arbeitskonto: 0,
        email: "x@example.org",
        status: "string",
        rechteklasse: "string",
        abteilung: "string",
        vertreter: "string"
    },
    {
        personalnummer: "02",
        name: "Hmmmm",
        vorname: "Müller",
        erreichbar: true,
        arbeitskonto: 0,
        email: "x@example.org",
        status: "string",
        rechteklasse: "string",
        abteilung: "string",
        vertreter: "string"
    }
];