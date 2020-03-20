import { Component, OnInit, Input } from '@angular/core';
import { Mitarbeiter, Arbeitsgruppe, apiAnswer } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';
import { DataService } from 'src/app/utils/services/data.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'dashboard-group-overview',
  templateUrl: './group-overview.component.html',
  styleUrls: ['./group-overview.component.scss']
})

export class GroupOverviewComponent implements OnInit {

  @Input() group: Arbeitsgruppe;
  public employees: Mitarbeiter[] = [];
  public leader: Mitarbeiter;
  public user: Mitarbeiter;
  public valid: boolean;
  public addToGroup: boolean = false;

  constructor(public api: ApiService,
    public dataService: DataService, ) {
  }

  ngOnInit(): void {
    this.user = this.dataService.getUser();
    this.valid = this.user.rechteklasse === "admin" || this.user.rechteklasse === "root";

    for (let m of this.group.mitglieder) {
      ///this.api.getUser(m).then(this.employees.push);
      this.api.getUser(m).then(answer => {
        if ((answer as apiAnswer)?.fehler) return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (answer as apiAnswer)?.fehler, "error");
        this.employees.push(answer as Mitarbeiter);
        if(m === this.group.leiter) this.leader = answer as Mitarbeiter;
      });
    }
  }

  public addUser(): void {
    this.addToGroup = !this.addToGroup;
  }

}
