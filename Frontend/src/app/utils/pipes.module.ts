import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReachablePipe } from './pipes/reachable.pipe';



@NgModule({
  declarations: [ReachablePipe],
  imports: [
    CommonModule
  ],
  exports: [
    ReachablePipe
  ]
})
export class PipesModule { }
