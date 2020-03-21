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
  user: Mitarbeiter;
  groups: Arbeitsgruppe[];
  vertreter: Mitarbeiter;

  constructor(public api: ApiService,
    private route: ActivatedRoute,
    private dataService: DataService,
    private titleService: Title) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(async params => {
      if (params.get("id") === "me") {
        this.user = this.dataService.getUser();
      }
      
      else {
        let res = await this.api.getUser(params.get("id"));
        if((res as apiAnswer)?.fehler) return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (res as apiAnswer)?.fehler, "error");
        this.user = res as Mitarbeiter;
      }
      this.titleService.setTitle(`Übersicht für ${this.user?.vorname} ${this.user?.name}`);

      let res: any = await this.api.getGroupsFromUser(this.user.personalnummer);
      if((res as apiAnswer)?.fehler) return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (res as apiAnswer)?.fehler, "error");
      this.groups = res as Arbeitsgruppe[];

      res = await this.api.getUser(this.user.vertreter);
      if((res as apiAnswer)?.fehler) return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (res as apiAnswer)?.fehler, "error");
      this.vertreter = res as Mitarbeiter;
    });
  }
}
