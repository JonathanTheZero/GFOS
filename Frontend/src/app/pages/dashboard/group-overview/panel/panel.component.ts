import { Component, OnInit, Input } from '@angular/core';
import { Mitarbeiter, apiAnswer } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';
import Swal from 'sweetalert2';
import { environment } from 'src/environments/environment';
import { employeeSamples } from 'src/app/utils/mock.data';

@Component({
  selector: 'dashboard-groups-panel',
  templateUrl: './panel.component.html',
  styleUrls: ['./panel.component.scss']
})

export class PanelComponent implements OnInit {
  @Input() group: string;
  public employees: Mitarbeiter[];
  public leader: Mitarbeiter;
  
  constructor(public api: ApiService) { }

  ngOnInit(): void {
    /*if(!environment.production){
      this.employees = employeeSamples;
      this.leader = employeeSamples[0];
      return;
    }*/
    this.api.getAllUsersFromGroup(this.group).then(answer => {
      if((answer as apiAnswer)?.fehler) return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (answer as apiAnswer)?.fehler, "error");
      this.leader = answer[0];
      this.employees = answer[1];
    });
  }

}
