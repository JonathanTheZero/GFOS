import { Component, OnInit, Input } from '@angular/core';
import { Arbeitsgruppe } from 'src/app/utils/interfaces/default.model';

@Component({
  selector: 'dashboard-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.scss']
})

export class GroupsComponent implements OnInit {
  @Input() groups: Array<Arbeitsgruppe>;
  
  constructor() { }

  ngOnInit(): void {
    this.groups = this.groups.sort((a, b) => a.arbeitsgruppenID > b.arbeitsgruppenID ? 1 : -1);
  }

}
