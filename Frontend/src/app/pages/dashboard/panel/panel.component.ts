import { Component, OnInit, Input } from '@angular/core';
import { Mitarbeiter } from 'src/app/utils/interfaces/default.model';

@Component({
  selector: 'dashboard-panel',
  templateUrl: './panel.component.html',
  styleUrls: ['./panel.component.scss']
})

export class PanelComponent implements OnInit {
  @Input() employees: Mitarbeiter[];
  public toggle: boolean = true;
  
  constructor() { }

  ngOnInit(): void {
  }

  public toggleView(): void {
    this.toggle = !this.toggle;
  }

}
