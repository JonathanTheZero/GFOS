import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReachablePipe } from './pipes/reachable.pipe';
import { EmployeeNamePipe } from './pipes/employee-name.pipe';
import { ListEmployeesPipe } from './pipes/list-employees.pipe';

const pipes = [
  ReachablePipe, 
  EmployeeNamePipe, 
  ListEmployeesPipe
]

@NgModule({
  declarations: [
    ...pipes
  ],
  imports: [
    CommonModule
  ],
  providers: [
    ...pipes
  ],
  exports: [
    ...pipes
  ]
})
export class PipesModule { }
