import { Component, OnInit, Input } from '@angular/core';
import { Mitarbeiter } from 'src/app/utils/interfaces/default.model';

@Component({
  selector: 'dashboard-department-mobile',
  templateUrl: './mobile.component.html',
  styleUrls: ['./mobile.component.scss']
})

export class MobileComponent implements OnInit {

  @Input() employees: Mitarbeiter[];
  constructor() { }

  ngOnInit(): void {
  }

}
