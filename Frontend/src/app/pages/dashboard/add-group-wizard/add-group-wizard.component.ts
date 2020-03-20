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

  @ViewChild("wizard") wizard: ClrWizard;
  @Input() open: boolean;
  @Output() openChange = new EventEmitter<boolean>();
  public model: any = {
    name: "",
    admin: "",
    mitglieder: []
  };
  public current: string;

  constructor(public api: ApiService) { }

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

  public removeEmployeeFromList(i: number | string) {
    this.model.mitglieder.splice(i, 1);
  }

  public addToList() {
    this.model.mitglieder.push(this.current);
    this.current = "";
  }

  public send(): void {
    this.api.addGroup(this.model.name, this.model.admin).then(async answer => {
      if ((answer as apiAnswer)?.fehler) {
        return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (answer as apiAnswer)?.fehler, "error");
      }

      let group: Arbeitsgruppe = answer as Arbeitsgruppe;
      const promises: Array<Promise<apiAnswer>> = [];
      for (let m of this.model.mitglieder) {
        promises.push(this.api.addToGroup(m, group.arbeitsgruppenID));
      }
      
      const res: Array<apiAnswer> = await Promise.all(promises);
      for (let r of res) {
        if (r.fehler) {
          return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + r.fehler, "error")
        }
      }
    });
  }
}
