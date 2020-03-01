import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { ClarityModule } from '@clr/angular';

import { LayoutComponent } from './layout/layout.component';
import { HeaderComponent } from './layout/header/header.component';
import { SidebarComponent } from './layout/sidebar/sidebar.component';
import { MainComponent } from './layout/main/main.component';

const components= [
  LayoutComponent,
  HeaderComponent,
  SidebarComponent,
  MainComponent,
]

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    ClarityModule,
  ],
  declarations: [
    ...components,
  ],
  exports: [
    LayoutComponent
  ],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ]
})

export class UiModule {

}