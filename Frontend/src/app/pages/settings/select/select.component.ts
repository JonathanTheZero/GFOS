import { Component, OnInit, Input } from '@angular/core';
import { options } from 'src/app/utils/interfaces/settings.model';
import Swal from 'sweetalert2';
import { DataService } from 'src/app/utils/services/data.service';

@Component({
  selector: 'settings-select',
  templateUrl: './select.component.html',
  styleUrls: ['./select.component.scss']
})

export class SelectComponent implements OnInit {

  @Input() options: options;
  @Input() type: "idle" | "logOut";
  public select: number;

  constructor(public dataService: DataService) { }

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
    if(this.type === "idle"){
      this.dataService.setIdle(this.select.toString());
    }
    else if (this.type === "logOut"){
      this.dataService.setTimeout(this.select.toString());
    }
    Swal.fire(
      "",
      "Ihre Einstellungen wurden übernommen",
      "success"
    );
  }

}
