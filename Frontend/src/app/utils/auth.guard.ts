import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import Swal from 'sweetalert2';
import { environment } from 'src/environments/environment';

@Injectable()
export class AuthGuard implements CanActivate {

    constructor(private router: Router) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        if(!environment.production){
          return true;
        }

        //user is logged in
        if (sessionStorage.getItem('currentUser')) {
            return true;
        }

        // not logged in so redirect to login page with the return url
        this.router.navigate(['/login'], { 
          queryParams: { 
            returnUrl: state.url 
          }
        });

        Swal.fire(
          "Fehler!", 
          "Sie sind nicht eingeloggt!", 
          "error"
        );
        return false;
    }
}