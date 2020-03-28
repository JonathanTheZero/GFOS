import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardComponent } from './dashboard.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ClarityModule } from '@clr/angular';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PipesModule } from 'src/app/utils/pipes.module';
import { GroupsComponent } from './groups/groups.component';
import { AddGroupWizardComponent } from './groups/add-group-wizard/add-group-wizard.component';
import { AddUserToGroupComponent } from './groups/add-user-to-group/add-user-to-group.component';
import { RemoveGroupComponent } from './groups/remove-group/remove-group.component';
import { AddGroupMobileComponent } from './mobile/add-group-mobile/add-group-mobile.component';
import { PanelsComponent } from './mobile/panels/panels.component';
import { EmployeeDatagridComponent } from './employee-datagrid/employee-datagrid.component';
import { HeaderComponent } from './header/header.component';

@NgModule({
  declarations: [
    DashboardComponent,
    GroupsComponent,
    AddGroupWizardComponent,
    AddUserToGroupComponent,
    RemoveGroupComponent,
    AddGroupMobileComponent,
    PanelsComponent,
    EmployeeDatagridComponent,
    HeaderComponent,
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

export class DashboardModule {

}
