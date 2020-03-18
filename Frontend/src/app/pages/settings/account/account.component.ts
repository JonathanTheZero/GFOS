import { Component, OnInit, Input, ViewChild } from "@angular/core";
import {
  FormGroup,
  FormBuilder,
  Validators,
  AbstractControl,
  NgForm,
} from "@angular/forms";
import { accountSettingsForm } from "src/app/utils/interfaces/settings.model";
import { ApiService } from "src/app/utils/services/api.service";
import { ClrForm } from '@clr/angular';
import { apiAnswer } from 'src/app/utils/interfaces/default.model';
import Swal from 'sweetalert2';

@Component({
  selector: "change-account-settings",
  templateUrl: "./account.component.html",
  styleUrls: ["./account.component.scss"]
})

export class AccountComponent implements OnInit {
  @Input() input: Array<accountSettingsForm>;
  @Input() onSubmit: "password" | "email";
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
    //validate input and give user success alert
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
          )
        );
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
          )
        );
      }
    }
  }

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


/**
 * custom validator in order to check wether the confirmation
 * equals the new password before sending it to the API 
 */
export function passwordMatchers(f: AbstractControl) {
  return f.get("newPassword").value === f.get("confirmNewPassword").value ?
    null : { passwordsNotEqual: true };
}