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

  //in/output for open or closing window
  @Input() open: boolean;
  @Output() openChange = new EventEmitter<boolean>();

  //form and input binding
  public createNewGroup: FormGroup;
  public members: string[] = [];
  public current: string = "";

  constructor(public api: ApiService,
    private formBuilder: FormBuilder) { }

    //initialize form group
  ngOnInit(): void {
    this.createNewGroup = this.formBuilder.group({
      name: ["", Validators.required],
      leader: ["", Validators.required]
    });
  }

  //close
  finish() {
    this.openChange.emit(false);
  }

  /**
   * send data to api (using the api service)
   * chaining requests for various employees -> validate result
   */
  public send(): void {
    this.api.addGroup(this.createNewGroup.value.name, this.createNewGroup.value.leader).then(async answer => {
      if ((answer as apiAnswer)?.fehler) {
        return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (answer as apiAnswer)?.fehler, "error");
      }

      let group: Arbeitsgruppe = answer as Arbeitsgruppe;
      const promises: Array<Promise<apiAnswer>> = [];
      this.members.forEach(val => promises.push(this.api.addToGroup(val, group.arbeitsgruppenID)));

      const res: Array<apiAnswer> = await Promise.all(promises);
      for (let r of res) {
        if (r.fehler) return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + r.fehler, "error");
      }
      return Swal.fire("", "Die Arbeitsgruppe wurde erfolgreich erstellt", "success");
    });
  }

  //close window
  close(): void {
    this.openChange.emit(false);
  }

  //remove from employees array
  public removeEmployeeFromList(i: number) {
    this.members.splice(i, 1);
  }

  //add to employees array
  public addToList() {
    this.members.push(this.current);
  }

}
