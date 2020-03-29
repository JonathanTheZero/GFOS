import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { trigger, transition, style, animate, state } from '@angular/animations';
import { Mitarbeiter, Arbeitsgruppe, apiAnswer } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';
import { DataService } from 'src/app/utils/services/data.service';
import Swal from 'sweetalert2';

@Component({
  selector: "app-dashboard",
  templateUrl: "./dashboard.component.html",
  styleUrls: ["./dashboard.component.scss"],
  animations: [
    //animation for enter/fading out of components
    trigger("inOutAnimation", [
      state(
        "invisible",
        style({ height: 0, opacity: 0, pointerEvents: "none" })
      ),
      state("visible", style({ height: "100%", opacity: 1 })),
      transition("invisible => visible", animate("1500ms")),
      transition("visible => invisible", animate("1000ms"))
    ])
  ]
})

export class DashboardComponent implements OnInit {
  //variables for template binding, names are self-explaining
  public isMobile: boolean;
  public user: Mitarbeiter;
  public groups: Array<Arbeitsgruppe>;
  public userGroups: Array<Arbeitsgruppe>;
  public departmentUsers: Mitarbeiter[] = [];

  //booleans for open or closing windows, arrays if there's more than one field
  public addToGroup: boolean[] = [];
  public removeFromGroup: boolean[] = [];
  public openAddGroup: boolean = false;

  //state changers using angular animations
  public viewMyGroups: "visible" | "invisible" = "invisible";
  public viewAllGroups: "visible" | "invisible" = "invisible";
  public viewDepartment: "visible" | "invisible" = "invisible";

  constructor(
    private titleService: Title,
    public api: ApiService,
    public dataService: DataService
  ) {}

  ngOnInit(): void {
    this.titleService.setTitle("Dashboard");
    this.dataService.isMobile().subscribe(m => (this.isMobile = m));
    this.user = this.dataService.getUser();
    this.requestGroups();
  }

  //re-request all groups and data
  public requestGroups(): void {
    this.api.getAllGroups().then(g => this.groups = g as Arbeitsgruppe[]);
    this.api.getGroupsFromUser().then(answer => {
      if ((answer as apiAnswer)?.fehler)
        return Swal.fire(
          "Fehler",
          "Es ist folgender Fehler aufgetreten: " +
            (answer as apiAnswer)?.fehler,
          "error"
        );
      //keeping groups up-to-date
      this.dataService.setGroups(answer as Arbeitsgruppe[]);
      this.userGroups = this.dataService.getGroups();

      this.addToGroup.fill(false, 0, this.userGroups?.length);
      this.removeFromGroup.fill(false, 0, this.userGroups?.length);
    });

    this.api.getDepartment().then(answer => {
      if ((answer as apiAnswer)?.fehler)
        return Swal.fire(
          "Fehler",
          "Es ist folgender Fehler aufgetreten: " +
            (answer as apiAnswer)?.fehler,
          "error"
        );
      this.departmentUsers = answer as Mitarbeiter[];
    });
  }

  //reload the page, triggered through children component events
  reload(r: boolean) {
    if (!r) return;
    this.user = this.dataService.getUser();
    this.requestGroups();
  }

  //toggle methods etc to openAddGroup various windows/modals/pop-ups etc
  //or other transitions and animations
  public openAddGroupWizard() {
    this.openAddGroup = !this.openAddGroup;
  }

  public addUser(index: number): void {
    this.addToGroup[index] = true;
  }

  public removeUser(index: number): void {
    this.removeFromGroup[index] = true;
  }

  public toggleAllGroups(): void {
    this.viewAllGroups =
      this.viewAllGroups === "visible" ? "invisible" : "visible";
  }

  public toggleMyGroups(): void {
    this.viewMyGroups =
      this.viewMyGroups === "visible" ? "invisible" : "visible";
  }

  public toggleDepartment(): void {
    this.viewDepartment =
      this.viewDepartment === "visible" ? "invisible" : "visible";
  }

}
