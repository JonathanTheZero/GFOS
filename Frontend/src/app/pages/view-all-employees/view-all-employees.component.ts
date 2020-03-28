import { Component, OnInit } from '@angular/core';
import { ClrDatagridSortOrder } from "@clr/angular";
import { Mitarbeiter, apiAnswer } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';
import Swal from 'sweetalert2';
import { DataService } from 'src/app/utils/services/data.service';

@Component({
  selector: 'app-view-all-employees',
  templateUrl: './view-all-employees.component.html',
  styleUrls: ['./view-all-employees.component.scss']
})

export class ViewAllEmployeesComponent implements OnInit {

  //bindings for HTML, what data to display etc, fetched in ngOnInit
  users: Mitarbeiter[];
  user: Mitarbeiter;
  total: number;
  loading: boolean = true;
  descSort = ClrDatagridSortOrder.DESC;
  models: ("root" | "admin" | "personnelDepartment" | "headOfDepartment" | "user")[] = []
  opts: string[] = ["admin", "personnelDepartment", "headOfDepartment", "user"];
  opened: boolean[] = [];

  constructor(public api: ApiService,
    public dataService: DataService) {

  }

  //initialize site with getting Data from services
  ngOnInit() {
    this.user = this.dataService.getUser();
    this.api.getAllUsers().then((answer: apiAnswer | Mitarbeiter[]) => {
      if ((answer as apiAnswer)?.fehler)
        return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (answer as apiAnswer)?.fehler, "error");
      this.users = answer as Mitarbeiter[];
      this.opened.fill(false, 0, this.users.length);
      this.loading = false;
    });
  }

  //open or close modal
  public openModal(index: number) {
    this.opened[index] = true;
  }

  public closeModal(index: number){
    this.opened[index] = false;
  }

  //sending data to api, validate answer
  public closeAndSend(index: number){
    this.closeModal(index);
    let u = this.users[index];
    this.api.alterAccess(this.models[index], u.personalnummer).then(answer => {
      if (answer.fehler)
        return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (answer as apiAnswer)?.fehler, "error");
      Swal.fire("", "Die Einstellungen wurden Erfoglreich übernommen", "success");
    });
  }
}
