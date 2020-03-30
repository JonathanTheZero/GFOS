import { Component, OnInit, EventEmitter, Output } from "@angular/core";
import { ApiService } from "src/app/utils/services/api.service";
import { DataService } from "src/app/utils/services/data.service";
import { Mitarbeiter } from "src/app/utils/interfaces/default.model";
import Swal from "sweetalert2";

@Component({
  selector: "dashboard-header-mobile",
  templateUrl: "./header.component.html",
  styleUrls: ["./header.component.scss"]
})

export class HeaderComponent implements OnInit {

  @Output() refresh = new EventEmitter<boolean>();
  user: Mitarbeiter;

  status: string;
  reason: string;
  hours: string;

  constructor(public api: ApiService, public dataService: DataService) { }

  ngOnInit(): void {
    this.user = this.dataService.getUser();
  }

  /*
    All 3 behaving similar: Sending specific request and validating output
  */
  public changeStatus(): void {
    this.api
      .alterStatus(this.status)
      .then(answer => {
        if (answer.fehler)
          return Swal.fire(
            "Fehler",
            "Es ist folgender Fehler aufgetreten: " + answer.fehler,
            "error"
          );
        Swal.fire("", "Ihr Status wurde erfolgreich übernommen", "success");
        this.dataService.getUser().status = this.status;
      })
      .then(() => this.refresh.emit(true));
  }

  public alterErreichbar(): void {
    this.api
      .alterReachable(!this.dataService.getUser().erreichbar)
      .then(answer => {
        if (answer.fehler)
          return Swal.fire(
            "Fehler",
            "Es ist folgender Fehler aufgetreten: " + answer.fehler,
            "error"
          );
        Swal.fire("", "Ihr Status wurde erfolgreich übernommen", "success");
        this.user.erreichbar = !this.user.erreichbar;
      })
      .then(() => this.refresh.emit(true));
  }

  public changeReason(): void {
    this.api
      .alterReason(this.reason)
      .then(answer => {
        if (answer.fehler)
          return Swal.fire(
            "Fehler",
            "Es ist folgender Fehler aufgetreten: " + answer.fehler,
            "error"
          );
        Swal.fire("", "Ihr Status wurde erfolgreich übernommen", "success");
        this.user.grundDAbw = this.reason;
      })
      .then(() => this.refresh.emit(true));
  }

  public addHours(): void {
    if (this.dataService.userSetHours) {
      Swal.fire("Fehler", "Sie haben Ihr Arbeitskonto heute bereits aktualisiert", "error");
      return;
    }
    if (parseInt(this.hours) > 12 || parseInt(this.hours) < 0 || isNaN(parseInt(this.hours))) {
      Swal.fire("Fehler", "Ungültige Stundenanzahl angegeben!", "error");
      return;
    }
    this.api.addHours(this.hours).then(answer => {
      if (answer.fehler)
        return Swal.fire(
          "Fehler",
          "Es ist folgender Fehler aufgetreten: " + answer.fehler,
          "error"
        );
      Swal.fire("", "Ihr Status wurde erfolgreich übernommen", "success");
      this.user.arbeitskonto += parseInt(this.hours);
      this.dataService.userSetHours = true;
    }).then(() => this.refresh.emit(true));
  }
  
}
