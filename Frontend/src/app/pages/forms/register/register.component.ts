import { Component, OnInit } from '@angular/core';
import { registerForm, errorObj } from 'src/app/utils/interfaces/register.model';
import { ApiService } from 'src/app/utils/services/api.service';
import { Title } from '@angular/platform-browser';
import { apiAnswer } from 'src/app/utils/interfaces/default.model';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  public form: registerForm = {
    name: "",
    lastName: "",
    password: "",
    type: "",
    email: "",
    accessLevel: null,
    district: "",
    agent: ""
  };

  public err: errorObj = {
    reason: ""
  }

  constructor(public api: ApiService,
    private titleService: Title,
    private router: Router) {
  }

  ngOnInit(): void {
    this.titleService.setTitle("Registrierung");
  }

  public validate(): void {
    if (!this.form.type) {
      this.err.reason = "Bitte wählen Sie eine zulässige Benutzergruppe!";
      return;
    }

    if (!this.form.name || !this.form.lastName) {
      this.err.reason = "Bitte geben Sie Ihrern vollständigen Namen an";
      return;
    }

    var re: RegExp = /.{1,}@.{1,}\..{1,}/mi;
    if (!re.test(this.form.email)) { //invalid E-Mail
      this.err.reason = "Bitte geben Sie eine zulässige E-Mail an!";
      return;
    }

    if (!this.form.password || this.form.password.length < 8) {
      this.err.reason = "Ihr Passwort muss mindestens 8 Zeichen lang sein!";
      return;
    }

    if(!this.form.district) {
      this.err.reason = "Bitte geben Sie eine Abteilung an!";
      return;
    }

    if (!["admin", "personalabteilung", "gruppenleiter", "user"].includes(this.form.accessLevel)) {
      this.err.reason = "Bitte wählen Sie eine Rechteklasse!";
      return;
    }

    if(!this.form.agent){
      this.err.reason = "Bitte geben Sie einen Vertreter an!";
      return;
    }

    const answer: apiAnswer = this.api.registerNewUser(
      this.form.lastName,
      this.form.name,
      this.form.email,
      this.form.password,
      this.form.accessLevel,
      this.form.district,
      this.form.agent
    );
    if (answer.fehler) {
      Swal.fire(
        "Es tut uns leid",
        "Bei der Verarbeitung Ihrer Anfrage ist leider etwas schiefgelaufen: Der Server meldete folgenden Grund: " + answer.fehler,
        "error"
      );
    }
    else {
      Swal.fire(
        "Ein neuer Account wurde registriert!",
        "Sie werden in kürze weitergeleitet.",
        "success"
      );
      setTimeout(() => this.router.navigate(['dashboard']), 5000);
    }
  }
}
