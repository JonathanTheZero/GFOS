import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { AuthGuard } from './utils/auth.guard';
import { SettingsComponent } from './pages/settings/settings.component';
import { RegisterComponent } from './pages/forms/register/register.component';
import { LoginComponent } from './pages/forms/login/login.component';

const routes: Routes = [
  {
    path: '',
    canActivate: [AuthGuard],
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
        component: DashboardComponent
      },
      {
        path: "settings",
        component: SettingsComponent,
      },
      {
        path: "api",
        redirectTo: "dashboard",
        pathMatch: "full"
      }
    ],
  },
  {
    path: "register",
    component: RegisterComponent
  },
  {
    path: "**",
    component: PageNotFoundComponent
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
