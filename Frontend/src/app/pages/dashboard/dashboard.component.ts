import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Mitarbeiter, Arbeitsgruppe } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';
import { DataService } from 'src/app/utils/services/data.service';
import { groupSamples } from 'src/app/utils/mock.data';

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
  open: boolean = false;
  samples: Array<Arbeitsgruppe> = groupSamples;

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

  public openWizard(){
    this.open = !this.open;
  }

}
