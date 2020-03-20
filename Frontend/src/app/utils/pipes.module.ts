import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReachablePipe } from './pipes/reachable.pipe';
import { EmployeeNamePipe } from './pipes/employee-name.pipe';



@NgModule({
  declarations: [
    ReachablePipe, 
    EmployeeNamePipe
  ],
  imports: [
    CommonModule
  ],
  exports: [
    ReachablePipe,
    EmployeeNamePipe
  ]
})
export class PipesModule { }
