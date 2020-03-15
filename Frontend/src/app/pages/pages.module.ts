import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { ClarityModule } from "@clr/angular";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

import { PageNotFoundComponent } from "./page-not-found/page-not-found.component";
import { DashboardModule } from './dashboard/dashboard.module';
import { CustomFormsModule } from './forms/custom-forms.module';
import { SettingsModule } from './settings/settings.module';
import { EmployeeStatsComponent } from './employee-stats/employee-stats.component';
import { ReachablePipe } from './employee-stats/reachable.pipe';
import { CreditsComponent } from './credits/credits.component';


const components = [
  PageNotFoundComponent,
  EmployeeStatsComponent,
  CreditsComponent
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
    CustomFormsModule,
    SettingsModule
  ],
  declarations: [
    ...components,
    ReachablePipe,
  ],
  exports: [
    ...components,
  ],
})
export class PagesModule { 

}
