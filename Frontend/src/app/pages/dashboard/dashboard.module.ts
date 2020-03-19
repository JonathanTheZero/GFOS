import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardComponent } from './dashboard.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ClarityModule } from '@clr/angular';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PanelComponent } from './panel/panel.component';
import { EmployeesComponent } from './employees/employees.component';
import { PipesModule } from 'src/app/utils/pipes.module';
import { GroupsComponent } from './groups/groups.component';
import { AddGroupWizardComponent } from './add-group-wizard/add-group-wizard.component';

@NgModule({
  declarations: [
    DashboardComponent, 
    PanelComponent,
    EmployeesComponent,
    GroupsComponent,
    AddGroupWizardComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    ClarityModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    PipesModule
  ],
  exports: [
    DashboardComponent
  ]
})
export class DashboardModule { }
