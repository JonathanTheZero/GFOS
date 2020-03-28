export interface routerLinks {
    title: string;
    icon: string;
    iconWhenClosed?: string;
    condition: string;
    links: Array<{
        link: string;
        title: string;
        icon: string;
    }>;
}