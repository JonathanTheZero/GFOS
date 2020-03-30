import { Component, OnInit, Output, EventEmitter } from "@angular/core";
import { ApiService } from "src/app/utils/services/api.service";
import { DataService } from "src/app/utils/services/data.service";
import { Mitarbeiter } from "src/app/utils/interfaces/default.model";
import Swal from "sweetalert2";

@Component({
  selector: "dashboard-header",
  templateUrl: "./header.component.html",
  styleUrls: ["./header.component.scss"]
})
export class HeaderComponent implements OnInit {
  public isMobile: boolean;
  public user: Mitarbeiter;
  public status: string;
  public reason?: string;
  public hours: string;
  //send true when dashboard should be refreshed
  @Output() refresh = new EventEmitter<boolean>();

  constructor(public api: ApiService, public dataService: DataService) { }

  ngOnInit(): void {
    this.dataService.isMobile().subscribe(m => (this.isMobile = m));
    this.user = this.dataService.getUser();
  }

  /*
    all methods are behaving similar: Sending a request and validating the answer/give user output
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
        this.user.status = this.status;
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
    if(parseInt(this.hours, 0) > 12 || parseInt(this.hours, 0) < 0) {
      Swal.fire(
        "Fehler",
        "Ungültige Stundenanzahl angegeben!",
        "error"
      );
      return;
    }
    this.api.addHours(this.hours).then(answer => {
      if (answer.fehler)
        return Swal.fire(
          "Fehler",
          "Es ist folgender Fehler aufgetreten: " + answer.fehler,
          "error"
        );
      Swal.fire("", "Ihr Arbeitskonto wurde aktualisiert", "success");
      this.user.arbeitskonto += parseInt(this.hours);
    }).then(() => this.refresh.emit(true));
  }
}
