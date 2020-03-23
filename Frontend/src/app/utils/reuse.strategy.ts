import { ActivatedRouteSnapshot, DetachedRouteHandle, RouteReuseStrategy } from '@angular/router';

/**
 * This class uses the Angular RouteReuseStrategy and hinders 
 * the routes from reloading when leaving (= they are cached)
 * this makes the website much faster and since the api service doesn't stream
 * the data, also doesn't affect the users experience
 */
export class CustomReuseStrategy implements RouteReuseStrategy {

    handlers: { 
        [key: string]: DetachedRouteHandle 
    } = {};

    shouldDetach(route: ActivatedRouteSnapshot): boolean {
        return route.data.shouldReuse || false;
    }

    store(route: ActivatedRouteSnapshot, handle: {}): void {
        if (route.data.shouldReuse) {
            this.handlers[route.routeConfig.path] = handle;
        }
    }

    shouldAttach(route: ActivatedRouteSnapshot): boolean {
        return !!route.routeConfig && !!this.handlers[route.routeConfig.path];
    }

    retrieve(route: ActivatedRouteSnapshot): {} {
        if (!route.routeConfig) return null;
        return this.handlers[route.routeConfig.path];
    }

    shouldReuseRoute(future: ActivatedRouteSnapshot, curr: ActivatedRouteSnapshot): boolean {
        return future.data.shouldReuse || false;
    }

}