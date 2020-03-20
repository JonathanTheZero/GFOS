import { Component, OnInit } from "@angular/core";
import { Mitarbeiter, apiAnswer } from "src/app/utils/interfaces/default.model";
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

  constructor(public api: ApiService,
    private route: ActivatedRoute,
    private dataService: DataService,
    private titleService: Title) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      if (params.get("id") === "me") {
        this.user = this.dataService.getUser();
        this.titleService.setTitle(`Übersicht für ${this.user.vorname} ${this.user.name}`)
      }
      else {
        this.api.getUser(params.get("id"))
          .then(answer => {
            if((answer as apiAnswer)?.fehler) return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (answer as apiAnswer)?.fehler, "error");
            this.user = answer as Mitarbeiter
          })
          .then(() => this.titleService.setTitle(`Übersicht für ${this.user?.vorname} ${this.user?.name}`));
      }
    });
  }
}
