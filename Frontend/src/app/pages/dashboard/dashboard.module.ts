import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardComponent } from './dashboard.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ClarityModule } from '@clr/angular';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PanelComponent } from './group-overview/panel/panel.component';
import { GroupOverviewComponent } from './group-overview/group-overview.component';
import { PipesModule } from 'src/app/utils/pipes.module';
import { GroupsComponent } from './groups/groups.component';
import { AddGroupWizardComponent } from './groups/add-group-wizard/add-group-wizard.component';
import { AddUserToGroupComponent } from './group-overview/add-user-to-group/add-user-to-group.component';
import { RemoveGroupComponent } from './groups/remove-group/remove-group.component';

@NgModule({
  declarations: [
    DashboardComponent, 
    PanelComponent,
    GroupOverviewComponent,
    GroupsComponent,
    AddGroupWizardComponent,
    AddUserToGroupComponent,
    RemoveGroupComponent
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
