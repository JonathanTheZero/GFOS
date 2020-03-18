import { Component, OnInit, Input } from '@angular/core';
import { Mitarbeiter } from 'src/app/utils/interfaces/default.model';

@Component({
  selector: 'dashboard-panel',
  templateUrl: './panel.component.html'
})
export class PanelComponent implements OnInit {
  @Input() employees: Mitarbeiter[];
  constructor() { }

  ngOnInit(): void {
  }

}
