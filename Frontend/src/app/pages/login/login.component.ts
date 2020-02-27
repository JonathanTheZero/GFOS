import { Component } from '@angular/core';
import { loginForm } from "../../utils/interfaces/login.model";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent {
 form : loginForm = {
   type: "",
   username: "",
   password: "",
   rememberMe: false
 };

  constructor() {
  }

  public submit(){
    alert(this.form.rememberMe);
  }
}