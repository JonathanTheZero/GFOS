import { Component, OnInit } from '@angular/core';
import { registerForm, errorObj } from 'src/app/utils/interfaces/register.model';
import { ApiService } from 'src/app/utils/services/api.service';
import { Title } from '@angular/platform-browser';
import { apiAnswer, Mitarbeiter } from 'src/app/utils/interfaces/default.model';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import { DataService } from 'src/app/utils/services/data.service';

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
    email: "",
    accessLevel: null,
    district: "",
    agent: ""
  };

  public err: errorObj = {
    reason: ""
  };

  public user: Mitarbeiter;

  constructor(public api: ApiService,
    private titleService: Title,
    private router: Router,
    public dataService: DataService) {
  }

  ngOnInit(): void {
    this.titleService.setTitle("Registrierung");
    this.dataService.getUser(true).subscribe(u => this.user = u);
  }

  /**
   * validate the input and then send the data to the API
   * using the apiService
   */
  public validate(): void {
    if (!this.form.name || !this.form.lastName) {
      this.err.reason = "Bitte geben Sie den vollständigen Namen an";
      return;
    }

    var re: RegExp = /.{1,}@.{1,}\..{1,}/mi;
    if (!re.test(this.form.email)) { //invalid E-Mail
      this.err.reason = "Bitte geben Sie eine zulässige E-Mail an!";
      return;
    }

    if (!this.form.password || this.form.password.length < 8) {
      this.err.reason = "Das Passwort muss mindestens 8 Zeichen lang sein!";
      return;
    }

    if(!this.form.district) {
      this.err.reason = "Bitte geben Sie eine Abteilung an!";
      return;
    }

    if (!this.form.accessLevel) {
      this.err.reason = "Bitte wählen Sie eine Rechteklasse!";
      return;
    }

    if(!this.form.agent || this.form.agent.length != 12){
      this.err.reason = "Bitte geben Sie eine/n Vertreter*in in Form seiner/ihrer 12-stelligen Personalnummer an!";
      return;
    }

    this.api.registerNewUser(
      this.form.lastName,
      this.form.name,
      this.form.email,
      this.form.password,
      this.form.accessLevel,
      this.form.district,
      this.form.agent
    ).then((answer: apiAnswer) => {
      if (answer.fehler) {
        Swal.fire(
          "Es tut uns leid",
          "Bei der Verarbeitung Ihrer Anfrage ist leider etwas schiefgelaufen: " + answer.fehler,
          "error"
        );
      }
      else {
        Swal.fire(
          "Ein neuer Account wurde registriert!",
          "Sie werden in kürze weitergeleitet.",
          "success"
        ).then(() => this.router.navigate(['dashboard']));
        setTimeout(() => this.router.navigate(['dashboard']), 5000);
      }
    });
  }
}
