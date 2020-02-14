import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { LoginComponent } from './pages/login/login.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { RegisterComponent } from './pages/register/register.component';
import { AuthGuard } from './utils/auth.guard';
import { SettingsComponent } from './pages/settings/settings.component';

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
        canActivate: [AuthGuard]
      },
      {
        path: "sing-up",
        component: RegisterComponent,
      },
      {
        path: "settings",
        component: SettingsComponent,
        canActivate: [AuthGuard]
      },
      {
        path: "api",
        redirectTo: "dashboard",
        pathMatch: "full"
      },
      {
        path: "**",
        component: PageNotFoundComponent
      },
      
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
