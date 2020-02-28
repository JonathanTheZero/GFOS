import { Component } from '@angular/core';
import { loginForm } from 'src/app/utils/interfaces/login.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent {
 form : loginForm = {
   username: "",
   password: ""
 };

  constructor() {
  }

  public submit(){

  }
}