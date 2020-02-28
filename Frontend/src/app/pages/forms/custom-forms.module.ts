import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterComponent } from './register/register.component';
import { RouterModule } from '@angular/router';
import { ClarityModule } from '@clr/angular';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from "./login/login.component";
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { ErrorComponent } from './error/error.component';


@NgModule({
  declarations: [
    RegisterComponent,
    LoginComponent,
    ErrorComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    ClarityModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    FormsModule
  ],
  exports: [
    RegisterComponent,
    LoginComponent
  ]
})
export class CustomFormsModule { }
