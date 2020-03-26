import { Component, OnInit, Input } from '@angular/core';
import { Mitarbeiter, apiAnswer } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';
import Swal from 'sweetalert2';

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
    this.api.getAllUsersFromGroup(this.group).then(answer => {
      if((answer as apiAnswer)?.fehler) return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (answer as apiAnswer)?.fehler, "error");
      this.leader = answer[0];
      this.employees = answer[1];
    });
  }

}
