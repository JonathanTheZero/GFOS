import { Component, OnInit, Input } from '@angular/core';
import { Arbeitsgruppe, Mitarbeiter, apiAnswer } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';
import { employeeSamples } from 'src/app/utils/mock.data';
import { environment } from 'src/environments/environment';
import Swal from 'sweetalert2';
import { DataService } from 'src/app/utils/services/data.service';
import { ClrDatagridSortOrder } from '@clr/angular';

@Component({
  selector: 'dashboard-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.scss']
})

export class GroupsComponent implements OnInit {

  @Input() groups: Array<Arbeitsgruppe>;
  public groupMembers: Array<Array<Mitarbeiter>> = [];
  public leaders: Array<Mitarbeiter> = [];
  public user: Mitarbeiter;
  public allowedToDelete: boolean;
  public password: string;
  public openModal: boolean[] = [];
  public descSort = ClrDatagridSortOrder.DESC;
  public loading: boolean = true;

  constructor(private api: ApiService,
    public dataService: DataService) { }

  ngOnInit(): void {
    this.groups = this.groups.sort((a, b) => a.arbeitsgruppenID > b.arbeitsgruppenID ? 1 : -1);
    this.openModal.fill(false, 0, this.groups.length)

    this.dataService.getUser(true).subscribe(u => this.user = u);
    this.allowedToDelete = ['root', 'admin'].includes(this.user.rechteklasse);

    const promises: Array<Array<Promise<Mitarbeiter>>> = [];
    const leaderPromises: Array<Promise<Mitarbeiter>> = [];

    this.groups.forEach((val, index) => {

      val.mitglieder.forEach((innerVal, i) => {
        if ((innerVal as apiAnswer)?.fehler) return;
        if (!promises[index]) promises[index] = [];
        if (!(val.leiter === innerVal))
          promises[index].push(this.api.getUser(innerVal) as Promise<Mitarbeiter>);
      });
      try {
        leaderPromises.push(this.api.getUser(val.leiter) as Promise<Mitarbeiter>);
      }
      catch {
        Swal.fire("Fehler", "Es ist ein Fehler aufgetreten", "error");
      }
    });

    Promise.all(leaderPromises).then(arr => this.leaders = arr);
    Promise.all(promises.map(Promise.all)).then(
      (arr: Mitarbeiter[][]) => this.groupMembers = arr
    ).then(_ => this.loading = false);
  }

  public deleteGroup(index: number){
    this.openModal[index] = true;
  }

}
