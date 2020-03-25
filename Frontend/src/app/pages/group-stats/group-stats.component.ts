import { Component, OnInit } from "@angular/core";
import {
  Arbeitsgruppe,
  Mitarbeiter,
  apiAnswer
} from "src/app/utils/interfaces/default.model";
import { ApiService } from "src/app/utils/services/api.service";
import { DataService } from "src/app/utils/services/data.service";
import { ActivatedRoute } from "@angular/router";
import Swal from "sweetalert2";

@Component({
  selector: "app-group-stats",
  templateUrl: "./group-stats.component.html",
  styleUrls: ["./group-stats.component.scss"]
})
export class GroupStatsComponent implements OnInit {
  public group: Arbeitsgruppe;
  public leader: Mitarbeiter;
  public members: Array<Mitarbeiter>;

  constructor(
    public api: ApiService,
    public dataService: DataService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(async params => {
      let id: string = params.get("id");

      this.api.getGroupFromID(id).then(res => {
        if ((res as apiAnswer)?.fehler)
          return Swal.fire(
            "Fehler",
            "Es ist folgender Fehler aufgetreten: " + (res as apiAnswer).fehler,
            "error"
          );
        this.group = res as Arbeitsgruppe;
      });

      this.api
        .getAllUsersFromGroup(id)
        .then(res => {
          if ((res as apiAnswer)?.fehler)
            return Swal.fire(
              "Fehler",
              "Es ist folgender Fehler aufgetreten: " +
                (res as apiAnswer).fehler,
              "error"
            );
          this.leader = res[0] as Mitarbeiter;
          this.members = res[1] as Mitarbeiter[];
        })
        .then(this.removeLeaderFromUsers);
    });
  }

  private removeLeaderFromUsers(): void {
    this.members = this.members.filter(
      m => m.personalnummer != this.leader.personalnummer
    );
  }
}
