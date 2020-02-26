import { Todo } from './interfaces/dashboard.model';

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