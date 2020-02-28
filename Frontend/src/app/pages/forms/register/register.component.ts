import { Component, OnInit } from '@angular/core';
import { registerForm, errorObj } from 'src/app/utils/interfaces/register.model';
import { ApiService } from 'src/app/utils/services/api.service';

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

  constructor(public api: ApiService) { 
  }

  ngOnInit(): void {

  }

  public validate(): void {
    var re : RegExp = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i; //check if string is E-Mail
    if(!re.test(this.form.email)){ //E-Mail is invalid

    }
    this.err.reason = "Test"
  }
}
