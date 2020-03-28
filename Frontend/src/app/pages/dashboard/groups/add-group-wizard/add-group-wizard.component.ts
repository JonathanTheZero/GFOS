import { Component, OnInit, ViewChild, Input, Output, EventEmitter } from '@angular/core';
import { ClrWizard } from '@clr/angular';
import { ApiService } from 'src/app/utils/services/api.service';
import Swal from 'sweetalert2';
import { apiAnswer, Arbeitsgruppe } from 'src/app/utils/interfaces/default.model';

@Component({
  selector: 'dashboard-add-group-wizard',
  templateUrl: './add-group-wizard.component.html',
  styleUrls: ['./add-group-wizard.component.scss']
})

export class AddGroupWizardComponent implements OnInit {

  //to control the form, for exact explanation see Angular and Clarity form docs
  @ViewChild("wizard") wizard: ClrWizard;
  @Input() open: boolean;
  @Output() openChange = new EventEmitter<boolean>();
  //template binding for form input
  public model: any = {
    name: "",
    admin: "",
    mitglieder: []
  };
  public current: string;

  constructor(public api: ApiService) { }

  ngOnInit(): void {
  }

  /**
   * reset form
   */
  finish() {
    this.openChange.emit(false);
    this.wizard.reset();
    this.model = {
      name: "",
      admin: "",
      mitglieder: []
    };
  }

  /**
   * remove from employee list
   * @param i index
   */
  public removeEmployeeFromList(i: number | string) {
    this.model.mitglieder.splice(i, 1);
  }

  /**
   * add to employee list 
   */
  public addToList() {
    this.model.mitglieder.push(this.current);
  }

  /**
   * send data and validate the api answering
   */
  public send(): void {
    this.api.addGroup(this.model.name, this.model.admin).then(async answer => {
      if ((answer as apiAnswer)?.fehler) {
        return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (answer as apiAnswer)?.fehler, "error");
      }

      let group: Arbeitsgruppe = answer as Arbeitsgruppe;
      const promises: Array<Promise<apiAnswer>> = [];
      this.model.mitglieder.forEach(val => promises.push(this.api.addToGroup(val, group.arbeitsgruppenID)));

      const res: Array<apiAnswer> = await Promise.all(promises);
      for (let r of res) {
        if (r.fehler) return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + r.fehler, "error");
      }
      return Swal.fire("", "Die Arbeitsgruppe wurde erfolgreich erstellt", "success");
    });
  }
}
