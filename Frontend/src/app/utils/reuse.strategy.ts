import { ActivatedRouteSnapshot, DetachedRouteHandle, RouteReuseStrategy } from '@angular/router';

/**
 * This class uses the Angular RouteReuseStrategy and hinders 
 * the routes from reloading when leaveing (= they are cached)
 * this makes the website much faster and since the api service doesn't stream
 * the data, also doesn't affect the users experience
 */
export class CustomReuseStrategy implements RouteReuseStrategy {

        handlers: {[key: string]: DetachedRouteHandle} = {};
    
        shouldDetach(route: ActivatedRouteSnapshot): boolean {
            console.debug('CustomReuseStrategy:shouldDetach', route);
            return true;
        }
    
        store(route: ActivatedRouteSnapshot, handle: DetachedRouteHandle): void {
            console.debug('CustomReuseStrategy:store', route, handle);
            this.handlers[route.routeConfig.path] = handle;
        }
    
        shouldAttach(route: ActivatedRouteSnapshot): boolean {
            console.debug('CustomReuseStrategy:shouldAttach', route);
            return !!route.routeConfig && !!this.handlers[route.routeConfig.path];
        }
    
        retrieve(route: ActivatedRouteSnapshot): DetachedRouteHandle {
            console.debug('CustomReuseStrategy:retrieve', route);
            if (!route.routeConfig) return null;
            return this.handlers[route.routeConfig.path];
        }
    
        shouldReuseRoute(future: ActivatedRouteSnapshot, curr: ActivatedRouteSnapshot): boolean {
            console.debug('CustomReuseStrategy:shouldReuseRoute', future, curr);
            return future.routeConfig === curr.routeConfig;
        }
    
}