import { Component, OnInit } from '@angular/core';
import { ClrDatagridSortOrder } from "@clr/angular";
import { Mitarbeiter, apiAnswer } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-view-all-employees',
  templateUrl: './view-all-employees.component.html',
  styleUrls: ['./view-all-employees.component.scss']
})

export class ViewAllEmployeesComponent implements OnInit {
  users: Mitarbeiter[];
  total: number;
  loading: boolean = true;
  descSort = ClrDatagridSortOrder.DESC;

  constructor(public api: ApiService) {

  }

  ngOnInit() {
    this.api.getAllUsers().then((answer: apiAnswer | Mitarbeiter[]) => {
      if ((answer as apiAnswer)?.fehler) 
        return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (answer as apiAnswer)?.fehler, "error");
      this.users = answer as Mitarbeiter[];
      this.loading = false;
    });
  }
}
