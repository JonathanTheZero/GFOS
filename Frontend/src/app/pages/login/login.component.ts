import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent {
 form : login = {
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

interface login {
  type: string;
  username: string;
  password: string;
  rememberMe: boolean;
}