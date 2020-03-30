export interface routerLinks {
    title: string;
    icon: string;
    iconWhenClosed?: string;
    links: Array<{
        link: string;
        title: string;
        icon: string;
    }>;
}