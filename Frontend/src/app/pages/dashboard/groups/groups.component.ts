import { Component, OnInit, Input } from '@angular/core';
import { Arbeitsgruppe, Mitarbeiter } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';
import { employeeSamples } from 'src/app/utils/mock.data';

@Component({
  selector: 'dashboard-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.scss']
})

export class GroupsComponent implements OnInit {
  @Input() groups: Array<Arbeitsgruppe>;
  public groupMembers: Array<Array<Mitarbeiter>>;
  public leaders: Array<Mitarbeiter>;
  
  constructor(private api: ApiService) { }

  ngOnInit(): void {
    this.groups = this.groups.sort((a, b) => a.arbeitsgruppenID > b.arbeitsgruppenID ? 1 : -1);
    

    this.groupMembers = [[...employeeSamples], [...employeeSamples]];
    this.leaders = employeeSamples;
    return;
    const promises: Array<Array<Promise<Mitarbeiter>>> = [];
    const leaderPromises: Array<Promise<Mitarbeiter>> = [];
    this.groups.forEach((val, index) => {

      val.mitglieder.forEach((innerVal, i) => {
        promises[index].push(this.api.getUser(innerVal));
      });

      leaderPromises.push(this.api.getUser(val.leiter));
    });

    Promise.all(leaderPromises).then(arr => this.leaders = arr);
    Promise.all(promises.map(Promise.all)).then(
      (arr: Mitarbeiter[][]) => this.groupMembers = arr
    );
  }

}
