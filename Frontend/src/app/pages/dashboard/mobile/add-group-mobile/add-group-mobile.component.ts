import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ApiService } from 'src/app/utils/services/api.service';
import { apiAnswer, Arbeitsgruppe } from 'src/app/utils/interfaces/default.model';
import Swal from 'sweetalert2';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'dashboard-add-group-mobile',
  templateUrl: './add-group-mobile.component.html',
  styleUrls: ['./add-group-mobile.component.scss']
})

export class AddGroupMobileComponent implements OnInit {

  @Input() mobile: boolean;
  @Input() open: boolean;
  @Output() openChange = new EventEmitter<boolean>();

  public createNewGroup: FormGroup;

  public current: string;

  constructor(public api: ApiService,
    private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.createNewGroup = this.formBuilder.group({
      name: ["", Validators.required],
      leader: ["", Validators.required],
      mitglieder: [[], Validators.required]
    });
  }

  finish() {
    this.openChange.emit(false);
  }

  public send(): void {
    this.api.addGroup(this.createNewGroup.value.name, this.createNewGroup.value.leader).then(async answer => {
      if ((answer as apiAnswer)?.fehler) {
        return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (answer as apiAnswer)?.fehler, "error");
      }

      let group: Arbeitsgruppe = answer as Arbeitsgruppe;
      const promises: Array<Promise<apiAnswer>> = [];
      this.createNewGroup.value.mitglieder.forEach(val => promises.push(this.api.addToGroup(val, group.arbeitsgruppenID)));

      const res: Array<apiAnswer> = await Promise.all(promises);
      for (let r of res) {
        if (r.fehler) return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + r.fehler, "error");
      }
      return Swal.fire("", "Die Arbeitsgruppe wurde erfolgreich erstellt", "success");
    });
  }

}
