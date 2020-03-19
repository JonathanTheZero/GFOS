import { Component, OnInit, ViewChild, Input } from '@angular/core';
import { ClrWizard } from '@clr/angular';

@Component({
  selector: 'dashboard-add-group-wizard',
  templateUrl: './add-group-wizard.component.html',
  styleUrls: ['./add-group-wizard.component.scss']
})

export class AddGroupWizardComponent implements OnInit {

  @ViewChild("wizardmd") wizardMedium: ClrWizard;
  @Input() openOnStart: boolean;
  open: boolean = this.openOnStart || false;

  constructor() { }

  ngOnInit(): void {
  }

}
