import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import Swal from 'sweetalert2';
import { environment } from 'src/environments/environment';
import { DataService } from './services/data.service';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private router: Router,
    private dataService: DataService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if(!environment.production){
      return true;
    }
    //user is logged in
    if (!this.dataService.getUser()) {
      return true;
    }

    if(this.router.url !== "/"){
      Swal.fire(
        "Fehler!",
        "Sie sind nicht eingeloggt!",
        "error"
      );
    }

    // not logged in so redirect to login page with the return url
    this.router.navigate(['/login'], {
      queryParams: {
        returnUrl: state.url
      }
    });

    return false;
  }
}