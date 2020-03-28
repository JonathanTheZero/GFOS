import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ApiService } from 'src/app/utils/services/api.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'dashboard-remove-group',
  templateUrl: './remove-group.component.html',
  styleUrls: ['./remove-group.component.scss']
})

export class RemoveGroupComponent implements OnInit {

  @Input() opened: boolean;
  @Output() openedChange = new EventEmitter<boolean>();
  @Output() refresh = new EventEmitter<boolean>();
  @Input() groupID: string;
  password: string = "";

  constructor(public api: ApiService) { }

  ngOnInit(): void {
  }

  close() {
    this.opened = false;
  }

  closeAndSend() {
    this.opened = false;
    this.api.deleteGroup(this.password, this.groupID).then((res) => {
      if (res?.fehler)
        return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + res.fehler, "error");
      Swal.fire("", "Die Gruppe wurde erfolgreich gelöscht", "success");
    }).then(() => this.refresh.emit(true));
  }
}
