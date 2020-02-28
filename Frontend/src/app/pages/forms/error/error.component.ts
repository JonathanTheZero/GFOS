import { Component, OnInit, Input } from '@angular/core';
import { errorObj } from 'src/app/utils/interfaces/register.model';

@Component({
  selector: 'form-error',
  templateUrl: './error.component.html'
})
export class ErrorComponent implements OnInit {
  @Input() error: errorObj;
  
  constructor() { }

  ngOnInit(): void {
  }

}
