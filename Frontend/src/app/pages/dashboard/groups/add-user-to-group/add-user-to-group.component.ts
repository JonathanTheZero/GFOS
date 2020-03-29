import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ApiService } from 'src/app/utils/services/api.service';
import { Arbeitsgruppe } from 'src/app/utils/interfaces/default.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'dashboard-add-user-to-group',
  templateUrl: './add-user-to-group.component.html',
  styleUrls: ['./add-user-to-group.component.scss']
})

export class AddUserToGroupComponent implements OnInit {

  //template binding and inputs for HTML
  @Input() group: Arbeitsgruppe;
  @Input() mode: "add" | "remove";
  @Input() opened: boolean;
  @Output() openedChange = new EventEmitter<boolean>();
  @Output() refresh = new EventEmitter<boolean>();
  public id: string;

  constructor(public api: ApiService) { }

  ngOnInit(): void {
    this.mode = this.mode || "add"; //setting default
  }

  /**
   * close form
   */
  public close(): void {
    this.opened = false;
  }

  /**
   * depending on mode send different request to api and validate result
   */
  public closeAndSend(): void {
    this.close();
    if (this.mode === "add") {
      this.api.addToGroup(this.id, this.group.arbeitsgruppenID).then((answer) => {
        if (answer?.fehler) return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + answer?.fehler, "error");
        Swal.fire("", "Der Mitarbeiter wurde erfolgreich hinzugefügt", "success");
      }).then(() => this.refresh.emit(true));
    }
    else if (this.mode === "remove") {
      this.api.removeFromGroup(this.id, this.group.arbeitsgruppenID).then((answer) => {
        if (answer?.fehler) return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + answer?.fehler, "error");
        Swal.fire("", "Der Mitarbeiter wurde erfolgreich entfernt", "success");
      }).then(() => this.refresh.emit(true));
    }
  }

}
