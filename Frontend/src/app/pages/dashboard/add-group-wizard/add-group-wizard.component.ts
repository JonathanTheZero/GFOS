import { Component, OnInit, ViewChild, Input, Output, EventEmitter } from '@angular/core';
import { ClrWizard } from '@clr/angular';

@Component({
  selector: 'dashboard-add-group-wizard',
  templateUrl: './add-group-wizard.component.html',
  styleUrls: ['./add-group-wizard.component.scss']
})

export class AddGroupWizardComponent implements OnInit {

  @ViewChild("wizard") wizard: ClrWizard;
  @Input() open: boolean;
  @Output() openChange = new EventEmitter<boolean>();
  public model: any = {
    name: "",
    admin: "",
    mitglieder: []
  };
  public current: string;

  constructor() { }

  ngOnInit(): void {
  }

  finish() {
    this.openChange.emit(false);
    this.wizard.reset();
    this.model = {
      name: "",
      admin: "",
      mitglieder: []
    };
  }

  public removeEmployeeFromList(i: number | string){
    this.model.mitglieder.splice(i, 1);
  }

  public addToList(){
    this.model.mitglieder.push(this.current);
    this.current = "";
  }
}
