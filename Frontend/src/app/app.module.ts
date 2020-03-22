import { BrowserModule, Title } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouteReuseStrategy } from '@angular/router';
import { ClarityModule } from '@clr/angular';
import { SweetAlert2Module } from '@sweetalert2/ngx-sweetalert2';
import { HttpClientModule }    from '@angular/common/http';
import { NgIdleModule } from '@ng-idle/core';

import { AppComponent } from './app.component';
import { UiModule } from './ui/ui.module';
import { AppRoutingModule } from './app-routing.module';
import { PagesModule } from './pages/pages.module';
import { AuthGuard } from './utils/auth.guard';
import { PipesModule } from "./utils/pipes.module";
import { CustomReuseStrategy } from './utils/reuse.strategy';


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
    PipesModule
  ],
  providers: [
    AuthGuard,
    Title,
    {
      provide: RouteReuseStrategy, 
      useClass: CustomReuseStrategy
    }
  ],
  bootstrap: [
    AppComponent
  ]
})

export class AppModule { 

}
