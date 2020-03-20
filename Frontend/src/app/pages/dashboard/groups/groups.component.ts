import { Component, OnInit, Input } from '@angular/core';
import { Arbeitsgruppe } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';

@Component({
  selector: 'dashboard-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.scss']
})

export class GroupsComponent implements OnInit {
  @Input() groups: Array<Arbeitsgruppe>;
  
  constructor(private api: ApiService) { }

  ngOnInit(): void {
    this.groups = this.groups.sort((a, b) => a.arbeitsgruppenID > b.arbeitsgruppenID ? 1 : -1);
  }

}
