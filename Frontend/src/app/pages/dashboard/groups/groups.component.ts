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
    this.openModal.fill(false, 0, this.groups.length)

    this.dataService.getUser(true).subscribe(u => this.user = u);
    this.allowedToDelete = ['root', 'admin'].includes(this.user.rechteklasse);

    for(let g of this.groups){
      this.api.getAllUsersFromGroup(g.arbeitsgruppenID).then(answer => {
        if((answer as apiAnswer)?.fehler) 
          return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (answer as apiAnswer).fehler, "error");
        this.groupMembers.push(answer[1] as Mitarbeiter[]);
        this.leaders.push(answer[0] as Mitarbeiter);
        this.loading = false;
      });
    }

  }

  public deleteGroup(index: number) {
    this.openModal[index] = true;
  }

}
