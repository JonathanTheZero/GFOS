import { Component, OnInit, Input } from '@angular/core';
import { Mitarbeiter } from 'src/app/utils/interfaces/default.model';

@Component({
  selector: 'dashboard-employees',
  templateUrl: './employees.component.html',
  styleUrls: ['./employees.component.scss']
})
export class EmployeesComponent implements OnInit {
  @Input() employees: Mitarbeiter[];
  a: any;
  b: any; c: any;
  d: any;
  constructor() { }

  ngOnInit(): void {
  }

}
