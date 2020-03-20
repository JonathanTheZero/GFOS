import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Mitarbeiter, Arbeitsgruppe } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';
import { DataService } from 'src/app/utils/services/data.service';
import { groupSamples } from 'src/app/utils/mock.data';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  employees: Mitarbeiter[];
  public isMobile: boolean;
  public user: Mitarbeiter;
  public open: boolean = false;
  public groups: Array<Arbeitsgruppe>;

  constructor(private titleService: Title,
    public api: ApiService,
    public dataService: DataService) { }

  ngOnInit(): void {
    this.titleService.setTitle("Dashboard");
    this.api.getEmployeeSamples().subscribe(x => this.employees = x);
    this.dataService.isMobile().subscribe(m => this.isMobile = m);
    if(!environment.production) this.groups = groupSamples;
    else this.api.getAllGroups().then(g => this.groups = g);

    this.user = this.dataService.getUser();
  }

  public openWizard(){
    this.open = !this.open;
  }

  public reload(){
    this.api.getAllGroups().then(g => this.groups = g);
  }

}
