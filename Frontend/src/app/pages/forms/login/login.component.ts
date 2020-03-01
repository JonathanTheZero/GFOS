import { Component, OnInit } from '@angular/core';
import { loginForm } from 'src/app/utils/interfaces/login.model';
import { Title } from '@angular/platform-browser';
import { errorObj } from 'src/app/utils/interfaces/register.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent implements OnInit {
  form: loginForm = {
    username: "",
    password: ""
  };

  public err: errorObj = {
    reason: ""
  }

  constructor(private titleService: Title) {
  }

  ngOnInit(): void {
    this.titleService.setTitle("Login");
  }

  public submit() {

  }
}