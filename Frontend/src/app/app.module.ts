import { BrowserModule, Title } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ClarityModule } from '@clr/angular';
import { SweetAlert2Module } from '@sweetalert2/ngx-sweetalert2';
import { HttpClientModule }    from '@angular/common/http';
import { NgIdleModule } from '@ng-idle/core';

import { AppComponent } from './app.component';
import { UiModule } from './ui/ui.module';
import { AppRoutingModule } from './app-routing.module';
import { PagesModule } from './pages/pages.module';
import { AuthGuard } from './utils/auth.guard';
import { DataService } from './utils/services/data.service';

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
    NgIdleModule.forRoot(),
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
