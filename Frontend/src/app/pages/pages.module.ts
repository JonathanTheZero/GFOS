import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { ClarityModule } from "@clr/angular";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

import { LoginComponent } from "./login/login.component";
import { PageNotFoundComponent } from "./page-not-found/page-not-found.component";
import { RegisterComponent } from "./register/register.component";
import { SettingsComponent } from './settings/settings.component';
import { DashboardModule } from './dashboard/dashboard.module';


const components = [
  LoginComponent,
  PageNotFoundComponent,
  RegisterComponent,
  SettingsComponent
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    ClarityModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    DashboardModule
  ],
  declarations: [
    ...components,
  ],
  exports: [
    ...components,
  ],
})
export class PagesModule { 

}
