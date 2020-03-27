import { Component, OnInit, Input } from '@angular/core';
import { Mitarbeiter, apiAnswer } from 'src/app/utils/interfaces/default.model';
import { ClrDatagridSortOrder } from '@clr/angular';
import { ApiService } from 'src/app/utils/services/api.service';
import { DataService } from 'src/app/utils/services/data.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'dashboard-employee-datagrid',
  templateUrl: './employee-datagrid.component.html',
  styleUrls: ['./employee-datagrid.component.scss']
})

export class EmployeeDatagridComponent implements OnInit {

  @Input() group?: string;
  @Input() employees?: Mitarbeiter[];
  @Input() mode: "group" | "department";
  public leader: Mitarbeiter;
  public user: Mitarbeiter;
  public valid: boolean;
  public ready: boolean = false;
  public descSort = ClrDatagridSortOrder.DESC;

  constructor(public api: ApiService,
    public dataService: DataService, ) {
  }

  ngOnInit(): void {
    this.user = this.dataService.getUser();

    if(this.mode === "group"){

      this.api.getAllUsersFromGroup(this.group).then((answer) => {
        if ((answer as apiAnswer)?.fehler) return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (answer as apiAnswer)?.fehler, "error");
        this.leader = answer[0];
        this.employees = answer[1];
        this.ready = true;
      });

    } else if(this.mode === "department"){
      this.ready = true;
    }

  }

}
