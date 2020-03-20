import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ApiService } from 'src/app/utils/services/api.service';

@Component({
  selector: 'dashboard-add-user-to-group',
  templateUrl: './add-user-to-group.component.html',
  styleUrls: ['./add-user-to-group.component.scss']
})

export class AddUserToGroupComponent implements OnInit {
  
  @Input() mode: "add" | "remove";
  @Input() opened: boolean;
  @Output() openedChange = new EventEmitter<boolean>();
  public id: string;

  constructor(public api: ApiService) { }

  ngOnInit(): void {
  }

  public close(): void {
    this.opened = false;
  }

  public closeAndSend(): void {
    this.close();
    //this.api.addToGroup(id)
  }

}
