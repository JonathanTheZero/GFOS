import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { LoginComponent } from './pages/login/login.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { RegisterComponent } from './pages/register/register.component';

const routes: Routes = [
  {
    path: '',
    children: [
      { 
        path: '', 
        redirectTo: '/login', 
        pathMatch: 'full' 
      },
      {
        path: "login",
        component: LoginComponent,
      },
      { 
        path: 'dashboard', 
        component: DashboardComponent,
      },
      {
        path: "sing-up",
        component: RegisterComponent,
      },
      {
        path: "**",
        component: PageNotFoundComponent
      }
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes),
  ],
  exports: [
    RouterModule,
  ],
})

export class AppRoutingModule {

}
