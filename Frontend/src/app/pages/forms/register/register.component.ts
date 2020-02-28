import { Component, OnInit } from '@angular/core';
import { registerForm, errorObj } from 'src/app/utils/interfaces/register.model';
import { ApiService } from 'src/app/utils/services/api.service';
import { Title } from '@angular/platform-browser';

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
    email: ""
  };

  public err: errorObj = {
    reason: ""
  }

  constructor(public api: ApiService,
    private titleService: Title) { 
  }

  ngOnInit(): void {
    this.titleService.setTitle("Registrierung");
  }

  public validate(): void {
    var re : RegExp = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i; //check if string is E-Mail
    if(!re.test(this.form.email)){ //E-Mail is invalid
      this.err.reason = "Bitte geben Sie eine zulässige E-Mail an!";
      return;
    }
    if(!this.form.type) this.err.reason = "Bitte wählen Sie eine zulässige Benutzergruppe!";
  }
}
