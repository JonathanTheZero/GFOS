import { Component, OnInit } from "@angular/core";
import { Mitarbeiter, apiAnswer, Arbeitsgruppe } from "src/app/utils/interfaces/default.model";
import { ApiService } from "src/app/utils/services/api.service";
import { ActivatedRoute } from "@angular/router";
import { DataService } from 'src/app/utils/services/data.service';
import { Title } from '@angular/platform-browser';
import Swal from 'sweetalert2';
import { groupSamples, employeeSamples } from 'src/app/utils/mock.data';

@Component({
  selector: "employee-stats",
  templateUrl: "./employee-stats.component.html",
  styleUrls: ["./employee-stats.component.scss"]
})

export class EmployeeStatsComponent implements OnInit {
  public user: Mitarbeiter;
  public groups: Arbeitsgruppe[];
  public vertreter: Mitarbeiter;
  public groupText: string;

  constructor(public api: ApiService,
    private route: ActivatedRoute,
    private dataService: DataService,
    private titleService: Title) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(async params => {

      if (params.get("id") === "me") {
        this.user = this.dataService.getUser();
        this.groupText = "Meine Arbeitsgruppen:";
        //this.groups = this.dataService.getGroups();
        this.titleService.setTitle(`Übersicht für ${this.user?.vorname} ${this.user?.name}`);
      } else {

        var res: any = await this.api.getUser(params.get("id"));
        if ((res as apiAnswer)?.fehler)
          return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (res as apiAnswer)?.fehler, "error");
        this.user = res as Mitarbeiter;

        //whether current user is allowed to see all groups or not
        if (["admin", "root"].includes(this.dataService.getUser().rechteklasse)) {
          res = await this.api.getGroupsFromUser(this.user.personalnummer);
          this.groupText = "Arbeitsgruppen:";
        } else {
          res = await this.api.getSharedGroups(this.user.personalnummer);
          this.groupText = "Gemeinsame Arbeitsgruppen:";
        }

        if ((res as apiAnswer)?.fehler)
          return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (res as apiAnswer)?.fehler, "error");
        this.groups = res as Arbeitsgruppe[];

        res = await this.api.getUser(this.user.vertreter);
        if ((res as apiAnswer)?.fehler)
          return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (res as apiAnswer)?.fehler, "error");
        this.vertreter = res as Mitarbeiter;
      }
    });
  }
}
