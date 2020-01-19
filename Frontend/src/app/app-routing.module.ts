import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { LoginComponent } from './pages/login/login.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { RegisterComponent } from './pages/register/register.component';
import { LoginGuard } from './pages/login.guard';

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
        //canActivate: [LoginGuard] 
      },
      { 
        path: 'dashboard', 
        component: DashboardComponent,
        //canActivate: [LoginGuard] 
      },
      {
        path: "sing-up",
        component: RegisterComponent,
        //canActivate: [LoginGuard] 
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
