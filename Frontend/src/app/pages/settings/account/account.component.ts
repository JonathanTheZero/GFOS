import { Component, OnInit, Input, ViewChild } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { accountSettingsForm } from "src/app/utils/interfaces/settings.model";
import { ApiService } from "src/app/utils/services/api.service";
import { ClrForm } from '@clr/angular';
import { apiAnswer } from 'src/app/utils/interfaces/default.model';
import Swal from 'sweetalert2';
import { passwordMatchers } from 'src/app/utils/validators';

@Component({
  selector: "change-account-settings",
  templateUrl: "./account.component.html",
  styleUrls: ["./account.component.scss"]
})

export class AccountComponent implements OnInit {

  //input on what type and what text to display
  @Input() input: Array<accountSettingsForm>;
  @Input() onSubmit: "password" | "email";
  //view child to deal with forms: more on this in Angular and Clarity docs
  @ViewChild(ClrForm, { static: true }) clrForm: ClrForm;

  form: FormGroup;

  constructor(private formBuilder: FormBuilder, public api: ApiService) {
  }

  /**
   * create form group based on type
   */
  ngOnInit(): void {
    if (this.onSubmit === "email") {
      this.form = this.formBuilder.group({
        email: ["", [Validators.required, Validators.email]],
        password: ["", Validators.required]
      });
    }
    else if (this.onSubmit === "password") {
      this.form = this.formBuilder.group(
        {
          password: ["", Validators.required],
          newPassword: ["", [Validators.required, Validators.minLength(8)]],
          confirmNewPassword: ["", [Validators.required, Validators.minLength(8)]]
        }, { validator: passwordMatchers }
      );
    }
  }

  submit() {
    //validate input and pass it to the api, give user feedback based on the answer
    if (this.form.valid) {
      if (this.onSubmit === "email") {
        this.api.changeEmail(
          this.form.value.password,
          this.form.value.email
        ).then(answer => this.fireChangeAlert(answer))
          .catch(() => Swal.fire(
            "Fehler",
            "Es konnte keine Verbindung zum Server hergestellt werden!",
            "error"
          ));
      }
      else if (this.onSubmit === "password") {
        this.api.changePassword(
          this.form.value.password,
          this.form.value.newPassword
        ).then(answer => this.fireChangeAlert(answer))
          .catch(() => Swal.fire(
            "Fehler",
            "Es konnte keine Verbindung zum Server hergestellt werden!",
            "error"
          ));
      }
    }
  }

  //just to make it a bit shorter
  private fireChangeAlert(answer: apiAnswer) {
    if (answer.fehler) {
      Swal.fire(
        "Fehler",
        "Der Server hat folgenden Fehler gemeldet: " + answer.fehler,
        "error"
      );
    }
    else if (answer.erfolg) {
      Swal.fire(
        "Änderungen Übernommen",
        "Ihre Änderungen wurden erfolgreich übernommen",
        "success"
      );
    }
  }

  reset() {
    this.form.setErrors(null);
  }

}