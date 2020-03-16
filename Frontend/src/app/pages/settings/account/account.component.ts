import { Component, OnInit, Input, ViewChild } from "@angular/core";
import {
  FormGroup,
  FormBuilder,
  Validators,
  AbstractControl,
} from "@angular/forms";
import { accountSettingsForm } from "src/app/utils/interfaces/settings.model";
import { ApiService } from "src/app/utils/services/api.service";
import { ClrForm } from '@clr/angular';

@Component({
  selector: "change-account-settings",
  templateUrl: "./account.component.html",
  styleUrls: ["./account.component.scss"]
})

export class AccountComponent implements OnInit {
  @Input() input: Array<accountSettingsForm>;
  @Input() onSubmit: "password" | "email";
  @ViewChild(ClrForm, { static: true }) clrForm;

  form: FormGroup;

  constructor(private formBuilder: FormBuilder, public api: ApiService) {
  }

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
    if (this.form.invalid) {
      this.clrForm.markAsDirty();
    } else {
      alert(true)
    }
  }

  reset() {
    this.form.reset();
  }
}

/**
 * custom validator in order to check wether the confirmation
 * equals the new password before sending it to the API 
 */
export function passwordMatchers (f: AbstractControl) {
  return f.get("newPassword").value === f.get("confirmNewPassword").value ? 
    null : { passwordsNotEqual: true };
 }