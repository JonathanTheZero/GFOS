import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { trigger, transition, style, animate, state } from '@angular/animations';
import { Mitarbeiter, Arbeitsgruppe, apiAnswer } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';
import { DataService } from 'src/app/utils/services/data.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  animations: [
    trigger(
      'inOutAnimation', [
      state('enter', style({ height: 0, opacity: 0, pointerEvents: "none" })),
      state('leave', style({ height: '100%', opacity: 1, pointerEvents: "none" })),
      transition('enter => leave', animate('1500ms')),
      transition('leave => enter', animate('1000ms'))
    ])
  ]
})

export class DashboardComponent implements OnInit {

  public isMobile: boolean;
  public user: Mitarbeiter;
  public open: boolean = false;
  public groups: Array<Arbeitsgruppe>;
  public userGroups: Array<Arbeitsgruppe>;
  public addToGroup: boolean[] = [];
  public removeFromGroup: boolean[] = [];

  public viewMyGroups: "leave" | "enter" = "enter";
  public viewAllGroups: "leave" | "enter" = "enter";

  public status: string;

  constructor(private titleService: Title,
    public api: ApiService,
    public dataService: DataService) { }


  ngOnInit(): void {
    this.titleService.setTitle("Dashboard");
    this.dataService.isMobile().subscribe(m => this.isMobile = m);
    this.user = this.dataService.getUser();
    this.requestGroups();
  }

  public openWizard() {
    this.open = !this.open;
  }

  public requestGroups(): void {
    this.api.getAllGroups().then(g => this.groups = g);

    this.api.getGroupsFromUser().then((answer) => {
      if ((answer as apiAnswer)?.fehler) return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (answer as apiAnswer)?.fehler, "error");
      this.userGroups = answer as Arbeitsgruppe[];

      //fill the binding for the modals
      this.addToGroup.fill(false, 0, this.userGroups.length);
      this.removeFromGroup.fill(false, 0, this.userGroups.length);
    });
  }

  public addUser(index: number): void {
    this.addToGroup[index] = true;
  }

  public removeUser(index: number): void {
    this.removeFromGroup[index] = true;
  }

  public toggleAllGroups(): void {
    this.viewAllGroups = this.viewAllGroups === 'leave' ? 'enter' : 'leave';
  }

  public toggleMyGroups(): void {
    this.viewMyGroups = this.viewMyGroups === 'leave' ? 'enter' : 'leave';
  }

  public changeStatus(): void {
    this.api.alterStatus(this.status).then(answer => {
      if (answer.fehler) return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + answer.fehler, "error");
      Swal.fire("", "Ihr Status wurde erfolgreich übernommen", "success");
      this.dataService.getUser().status = this.status;
    });
  }

  public alterErreichbar(): void {
    this.api.alterReachable(!this.dataService.getUser().erreichbar).then(answer => {
      if (answer.fehler) return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + answer.fehler, "error");
      Swal.fire("", "Ihr Status wurde erfolgreich übernommen", "success");
      this.dataService.getUser().erreichbar = !this.dataService.getUser().erreichbar;
    });
  }

}
