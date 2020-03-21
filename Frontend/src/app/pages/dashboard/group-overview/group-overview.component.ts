import { Component, OnInit, Input } from '@angular/core';
import { Mitarbeiter, Arbeitsgruppe, apiAnswer } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';
import { DataService } from 'src/app/utils/services/data.service';
import Swal from 'sweetalert2';
import { employeeSamples } from 'src/app/utils/mock.data';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'dashboard-group-overview',
  templateUrl: './group-overview.component.html',
  styleUrls: ['./group-overview.component.scss']
})

export class GroupOverviewComponent implements OnInit {

  @Input() group: string;
  public employees: Mitarbeiter[] = [];
  public leader: Mitarbeiter;
  public user: Mitarbeiter;
  public valid: boolean;
  public ready: boolean = false;

  constructor(public api: ApiService,
    public dataService: DataService, ) {
  }

  ngOnInit(): void {
    this.user = this.dataService.getUser();
    this.valid = this.user.rechteklasse === "admin" || this.user.rechteklasse === "root";
    
    if(!environment.production){
      this.employees = employeeSamples;
      this.leader = employeeSamples[0];
      this.ready = true;
      return;
    }

    this.api.getAllUsersFromGroup(this.group).then((answer) => {
      if((answer as apiAnswer)?.fehler) return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (answer as apiAnswer)?.fehler, "error");
      this.leader = answer[0];
      this.employees = answer[1];
      this.ready = true;
    });
  }

}
