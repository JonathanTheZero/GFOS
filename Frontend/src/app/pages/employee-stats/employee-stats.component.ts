import { Component, OnInit } from "@angular/core";
import { Mitarbeiter, apiAnswer, Arbeitsgruppe } from "src/app/utils/interfaces/default.model";
import { ApiService } from "src/app/utils/services/api.service";
import { ActivatedRoute } from "@angular/router";
import { DataService } from 'src/app/utils/services/data.service';
import { Title } from '@angular/platform-browser';
import Swal from 'sweetalert2';

@Component({
  selector: "employee-stats",
  templateUrl: "./employee-stats.component.html",
  styleUrls: ["./employee-stats.component.scss"]
})

export class EmployeeStatsComponent implements OnInit {

  public employee: Mitarbeiter;
  public user: Mitarbeiter;
  public groups: Arbeitsgruppe[];
  public vertreter: Mitarbeiter;
  public groupText: string;

  //binding for the modals
  public openedRep = false;
  public openedDep = false;
  public newRep: string;
  public newDep: string;

  constructor(public api: ApiService,
    private route: ActivatedRoute,
    private dataService: DataService,
    private titleService: Title) { }

  ngOnInit(): void {
    //subscribe to route changes
    this.user = this.dataService.getUser();
    this.route.paramMap.subscribe(async params => {

      //if route is me, fetch data from dataService, not from the api -> faster
      if (params.get("id") === "me") {
        this.employee = this.dataService.getUser();
        this.groupText = "Meine Arbeitsgruppen:";
        this.groups = this.dataService.getGroups();
        this.titleService.setTitle(`Übersicht für ${this.employee?.vorname} ${this.employee?.name}`);

        let res = await this.api.getUser(this.employee.vertreter);
        if ((res as apiAnswer)?.fehler)
          return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (res as apiAnswer)?.fehler, "error");
        this.vertreter = res as Mitarbeiter;
      } else {
        //otherwise fetch from the api, depending on the route params, just fetch from API and validate answer
        var res: any = await this.api.getUser(params.get("id"));
        if ((res as apiAnswer)?.fehler)
          return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (res as apiAnswer)?.fehler, "error");
        this.employee = res as Mitarbeiter;

        //whether current employee is allowed to see all groups or not
        if (["admin", "root"].includes(this.dataService.getUser().rechteklasse)) {
          res = await this.api.getGroupsFromUser(this.employee.personalnummer);
          this.groupText = "Arbeitsgruppen:";
        } else {
          res = await this.api.getSharedGroups(this.employee.personalnummer);
          this.groupText = "Gemeinsame Arbeitsgruppen:";
        }

        if ((res as apiAnswer)?.fehler && (answer as apiAnswer)?.fehler !== "Leere Rückgabe der Datenbank.")
          return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (res as apiAnswer)?.fehler, "error");
        this.groups = res as Arbeitsgruppe[];

        res = await this.api.getUser(this.employee.vertreter);
        if ((res as apiAnswer)?.fehler && (answer as apiAnswer)?.fehler !== "Leere Rückgabe der Datenbank.")
          return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (res as apiAnswer)?.fehler, "error");
        this.vertreter = res as Mitarbeiter;
      }

    });
  }

  openRepModal(){
    this.openedRep = true;
  }

  closeRepModal(){
    this.openedRep = false;
  }

  closeAndSendRep(){
    this.openedRep = false;
    this.api.alterRep(this.newRep, this.employee.personalnummer).then(answer => {
      if(answer.fehler)
        return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + answer.fehler, "error");
      Swal.fire("", "Ihre Änderungen wurden erfolgreich übernommen", "success");
    });
    this.user.vertreter = this.newRep;
  }

  openDepModal(){
    this.openedDep = true;
  }

  closeDepModal(){
    this.openedDep = false;
  }

  closeAndSendDep(){
    this.openedDep = false;
    this.api.alterDep(this.newDep, this.employee.personalnummer).then(answer => {
      if(answer.fehler)
        return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + answer.fehler, "error");
      Swal.fire("", "Ihre Änderungen wurden erfolgreich übernommen", "success");
    });
    this.user.abteilung = this.newDep;
  }
}
