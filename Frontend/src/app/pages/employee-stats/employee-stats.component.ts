import { Component, OnInit } from "@angular/core";
import { Mitarbeiter } from "src/app/utils/interfaces/default.model";
import { ApiService } from "src/app/utils/services/api.service";
import { ActivatedRoute } from "@angular/router";

@Component({
  selector: "employee-stats",
  templateUrl: "./employee-stats.component.html",
  styleUrls: ["./employee-stats.component.scss"]
})
export class EmployeeStatsComponent implements OnInit {
  user: Mitarbeiter;
  pn: number | string;

  constructor(public api: ApiService, 
    private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params =>{
      this.pn = params.get("id");
      this.api.getUser(this.pn, true).subscribe(x => this.user = x);
    });
  }
}
