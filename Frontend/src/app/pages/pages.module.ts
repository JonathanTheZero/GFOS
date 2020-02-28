import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { ClarityModule } from "@clr/angular";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

import { PageNotFoundComponent } from "./page-not-found/page-not-found.component";
import { SettingsComponent } from './settings/settings.component';
import { DashboardModule } from './dashboard/dashboard.module';
import { CustomFormsModule } from './forms/custom-forms.module';


const components = [
  PageNotFoundComponent,
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
    DashboardModule,
    CustomFormsModule
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
