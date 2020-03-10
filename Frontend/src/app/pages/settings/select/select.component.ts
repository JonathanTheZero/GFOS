import { Component, OnInit, Input } from '@angular/core';
import { options } from 'src/app/utils/interfaces/settings.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'settings-select',
  templateUrl: './select.component.html',
  styleUrls: ['./select.component.scss']
})
export class SelectComponent implements OnInit {

  @Input() options: options;
  @Input() type: "idle" | "logOut";
  public select;

  constructor() { }

  ngOnInit(): void {
  }

  public changeSettings(): void {
    if (!this.select) {
      Swal.fire(
        "Fehler",
        "Bitte wählen Sie zuerst eine Option aus",
        "error"
      );
      return;
    }
    localStorage.setItem(this.type, this.select);
    Swal.fire(
      "",
      "Ihre Einstellungen wurden übernommen",
      "success"
    );
  }

}
