import { Component, OnInit } from '@angular/core';
import { ApiService } from './utils/services/api.service';
import { apiAnswer } from './utils/interfaces/default.model';
import { UserIdleService } from 'angular-user-idle';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})

export class AppComponent implements OnInit {
  title = 'Dashboard';

  constructor(private api: ApiService,
    private userIdle: UserIdleService,
    private router: Router){

  }

  ngOnInit(): void {
    window.addEventListener("beforeunload", e => {
      //logout for the user if needed
      if(sessionStorage.getItem("currentUser")){
        const logout: apiAnswer = this.api.logout(sessionStorage.getItem("currentUser"));
        if(logout.erfolg){
          sessionStorage.removeItem("currentUser");
        }
        else {
          return logout.fehler;
        }
      }                           
    });
    this.userIdle.startWatching();
    this.userIdle.onTimerStart().subscribe(count => console.log(count));
    this.userIdle.onTimeout().subscribe(() => {
      Swal.fire(
        "Inaktivität",
        "Aufgrund von Inaktivität wurden Sie automatisch ausgeloggt. Sie werden nun zur Login-Seite weitergeleitet",
        "info"
      ).then(() => this.router.navigate(['login']));
      this.api.logout(sessionStorage.getItem("currentUser"));
    });
  }
}
