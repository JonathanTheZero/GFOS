import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardComponent } from './dashboard.component';
import { TodosComponent } from './todos/todos.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ClarityModule } from '@clr/angular';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PanelComponent } from './panel/panel.component';
import { EmployeesComponent } from './employees/employees.component';

@NgModule({
  declarations: [
    DashboardComponent, 
    TodosComponent,
    PanelComponent,
    EmployeesComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    ClarityModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
  ],
  exports: [
    DashboardComponent
  ]
})
export class DashboardModule { }
