import { Component, OnInit, Input } from '@angular/core';
import { Arbeitsgruppe } from 'src/app/utils/interfaces/default.model';

@Component({
  selector: 'dashboard-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.scss']
})
export class GroupsComponent implements OnInit {
  @Input() group: Arbeitsgruppe;
  
  constructor() { }

  ngOnInit(): void {
  }

}
