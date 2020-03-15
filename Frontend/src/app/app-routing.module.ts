import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";

import { DashboardComponent } from "./pages/dashboard/dashboard.component";
import { PageNotFoundComponent } from "./pages/page-not-found/page-not-found.component";
import { AuthGuard } from "./utils/auth.guard";
import { SettingsComponent } from "./pages/settings/settings.component";
import { RegisterComponent } from "./pages/forms/register/register.component";
import { LoginComponent } from "./pages/forms/login/login.component";
import { EmployeeStatsComponent } from './pages/employee-stats/employee-stats.component';
import { CreditsComponent } from './pages/credits/credits.component';

const routes: Routes = [
  {
    path: "login",
    component: LoginComponent
  },
  {
    path: "",
    canActivate: [AuthGuard],
    children: [
      {
        path: "",
        redirectTo: "/dashboard",
        pathMatch: "full"
      },
      {
        path: "dashboard",
        component: DashboardComponent
      },
      {
        path: "settings",
        component: SettingsComponent
      },
      {
        path: "register",
        component: RegisterComponent
      },
      {
        path: "employee/:id",
        component: EmployeeStatsComponent
      },
      {
        path: "credits",
        component: CreditsComponent
      },
      {
        path: "api",
        redirectTo: "dashboard",
        pathMatch: "full"
      }
    ]
  },
  {
    path: "**",
    component: PageNotFoundComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
