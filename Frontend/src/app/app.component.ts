import { Component, OnInit } from '@angular/core';
import { ApiService } from './utils/services/api.service';
import { apiAnswer } from './utils/interfaces/default.model';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import { Idle, DEFAULT_INTERRUPTSOURCES } from '@ng-idle/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})

export class AppComponent implements OnInit {

  title = 'Dashboard';
  idleState: string = 'Not started.';
  timedOut: boolean = false;

  constructor(private api: ApiService,
    private router: Router,
    private idle: Idle) {
  }

  ngOnInit(): void {
    window.addEventListener("beforeunload", e => {
      //user will get logged out if the window is closed
      if (sessionStorage.getItem("currentUser")) {
        const logout: apiAnswer = this.api.logout(sessionStorage.getItem("currentUser"));
        if (logout.erfolg) {
          sessionStorage.removeItem("currentUser");
        }
        else {
          return logout.fehler;
        }
      }
    });

    // After 10 minutes of inactivity the countdown is started
    this.idle.setIdle(600);
    // the user gets a warning, after 2 additional minutes, the user is logged out
    this.idle.setTimeout(120);
    // countdown will be interrupted through mousehover, keyboard etc
    this.idle.setInterrupts(DEFAULT_INTERRUPTSOURCES);

    this.idle.onIdleEnd.subscribe(() => {
      this.idleState = 'No longer idle.'
      console.log(this.idleState);
      this.reset();
    });

    this.idle.onTimeout.subscribe(() => {
      this.idleState = 'Timed out!';
      this.timedOut = true;
      console.log(this.idleState);
      Swal.fire(
        "Inaktivität",
        "Aufgrund von Inaktivität wurden Sie automatisch ausgeloggt. Sie werden nun zur Login-Seite weitergeleitet",
        "info"
      ).then(() => this.router.navigate(['login']));
    });

    this.idle.onIdleStart.subscribe(() => {
      this.idleState = 'You\'ve gone idle!'
      console.log(this.idleState);
      Swal.fire(
        "Inaktivität",
        "Aufgrund von Inaktivität werden Sie in kürze automatisch ausgeloggt",
        "info"
      ).then(() => this.reset()) //if the user is online again
    });

    this.idle.onTimeoutWarning.subscribe(countdown => {
      this.idleState = 'You will time out in ' + countdown + ' seconds!'
      console.log(this.idleState);
    });

    this.reset();
  }

  private reset() {
    this.idle.watch();
    this.idleState = 'Started.';
    this.timedOut = false;
  }
}
