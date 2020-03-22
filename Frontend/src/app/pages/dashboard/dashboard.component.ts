import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { trigger, transition, style, animate } from '@angular/animations';
import { Mitarbeiter, Arbeitsgruppe, apiAnswer } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';
import { DataService } from 'src/app/utils/services/data.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})

export class DashboardComponent implements OnInit {

  public isMobile: boolean;
  public user: Mitarbeiter;
  public open: boolean = false;
  public groups: Array<Arbeitsgruppe>;
  public userGroups: Array<Arbeitsgruppe>;
  public addToGroup: boolean[] = [];
  public removeFromGroup: boolean[] = [];
  
  public viewMyGroups: boolean = false;
  public viewAllGroups: boolean = false;

  constructor(private titleService: Title,
    public api: ApiService,
    public dataService: DataService) { }


  ngOnInit(): void {
    this.titleService.setTitle("Dashboard");
    this.dataService.isMobile().subscribe(m => this.isMobile = m);
    this.user = this.dataService.getUser();
    this.requestGroups();
  }

  public openWizard() {
    this.open = !this.open;
  }

  public requestGroups(): void {
    this.api.getAllGroups().then(g => this.groups = g);

    this.api.getGroupsFromUser().then((answer) => {
      if ((answer as apiAnswer)?.fehler) return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (answer as apiAnswer)?.fehler, "error");
      this.userGroups = answer as Arbeitsgruppe[];

      //fill the binding for the modals
      this.addToGroup.fill(false, 0, this.userGroups.length);
      this.removeFromGroup.fill(false, 0, this.userGroups.length);
    });
  }

  public addUser(index: number): void {
    this.addToGroup[index] = true;
  }

  public removeUser(index: number): void {
    this.removeFromGroup[index] = true;
  }

  public toggleMyGroups(){
    this.viewMyGroups = !this.viewMyGroups;
  }

  public toggleAllGroups(){
    this.viewAllGroups = !this.viewAllGroups;
  }

}
