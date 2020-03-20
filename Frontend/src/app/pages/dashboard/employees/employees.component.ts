import { Component, OnInit, Input } from '@angular/core';
import { Mitarbeiter } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';
import { DataService } from 'src/app/utils/services/data.service';

@Component({
  selector: 'dashboard-employees',
  templateUrl: './employees.component.html',
  styleUrls: ['./employees.component.scss']
})

export class EmployeesComponent implements OnInit {
  @Input() employees: Mitarbeiter[];
  public user: Mitarbeiter;
  public valid: boolean;
  public addToGroup: boolean = false;

  constructor(public api: ApiService,
    public dataService: DataService,) { 
  }

  ngOnInit(): void {
    this.user = this.dataService.getUser();
    this.valid = this.user.rechteklasse === "admin" || this.user.rechteklasse === "root";
  }

  public addUser(): void {
    this.addToGroup = !this.addToGroup;
  }

}
