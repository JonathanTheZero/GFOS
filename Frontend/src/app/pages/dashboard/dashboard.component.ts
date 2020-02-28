import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Mitarbeiter } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  employees: Mitarbeiter[];

  constructor(private titleService: Title,
    public api: ApiService) { }

  ngOnInit(): void {
    this.titleService.setTitle("Dashboard");
    this.api.getEmployeeSamples().subscribe(x => this.employees = x);
  }

}
