import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Mitarbeiter } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';
import { DataService } from 'src/app/utils/services/data.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  employees: Mitarbeiter[];
  public isMobile: boolean;
  public user: Mitarbeiter;
  public valid: boolean;
  x: boolean = false;

  constructor(private titleService: Title,
    public api: ApiService,
    public dataService: DataService) { }

  ngOnInit(): void {
    this.titleService.setTitle("Dashboard");
    this.api.getEmployeeSamples().subscribe(x => this.employees = x);
    this.dataService.isMobile().subscribe(m => this.isMobile = m);

    this.user = this.dataService.getUser();
    this.valid = this.user.rechteklasse === "admin" || this.user.rechteklasse === "root";
  }

}
