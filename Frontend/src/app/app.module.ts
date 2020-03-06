import { BrowserModule, Title } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ClarityModule } from '@clr/angular';
import { SweetAlert2Module } from '@sweetalert2/ngx-sweetalert2';
import { HttpClientModule }    from '@angular/common/http';
import { UserIdleModule } from 'angular-user-idle';

import { AppComponent } from './app.component';
import { UiModule } from './ui/ui.module';
import { AppRoutingModule } from './app-routing.module';
import { PagesModule } from './pages/pages.module';
import { AuthGuard } from './utils/auth.guard';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ClarityModule,
    UiModule,
    PagesModule,
    AppRoutingModule,
    SweetAlert2Module.forRoot(),
    HttpClientModule,
    UserIdleModule.forRoot({
      idle: 600, 
      timeout: 120, 
      ping: 120
    }),
  ],
  providers: [
    AuthGuard,
    Title
  ],
  bootstrap: [
    AppComponent
  ]
})

export class AppModule { 

}
