import { Component, OnInit, Input } from '@angular/core';
import { Mitarbeiter } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';

@Component({
  selector: 'dashboard-employees',
  templateUrl: './employees.component.html',
  styleUrls: ['./employees.component.scss']
})
export class EmployeesComponent implements OnInit {
  @Input() employees: Mitarbeiter[];
  user: Mitarbeiter;
  public valid: boolean;

  constructor(public api: ApiService) { }

  ngOnInit(): void {
    this.api.getCurrentUser().subscribe(answer => this.user = answer);
    if(this.user.rechteklasse == "root"){
      this.valid = true;
    }
  }

}
