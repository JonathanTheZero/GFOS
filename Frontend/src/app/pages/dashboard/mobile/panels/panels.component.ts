import { Component, OnInit, Input } from '@angular/core';
import { Mitarbeiter, apiAnswer } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'dashboard-mobile-panels',
  templateUrl: './panels.component.html',
  styleUrls: ['./panels.component.scss']
})

export class PanelsComponent implements OnInit {

  //inputs optional but one of those should be given
  //either displaying given employees or reqeusting those of
  //the group whichs ID was given
  @Input() employees?: Mitarbeiter[];
  @Input() group?: string;
  
  constructor(public api: ApiService) { }

  ngOnInit(): void {
    if(this.group){
      this.api.getAllUsersFromGroup(this.group).then(answer => {
        if((answer as apiAnswer)?.fehler) return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (answer as apiAnswer)?.fehler, "error");
        this.employees = answer[1];
      });
    }
  }

}
