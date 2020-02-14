import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, Form } from '@angular/forms';
import { ClarityModule } from '@clr/angular';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  
 // form: FormGroup;
 form : any = {
   type: "",
   username: "",
   password: "",
   rememberMe: ""
 };

  constructor(private formBuilder: FormBuilder) {
    
  }

  public submit(){
    alert(this.form.username);
  }
}