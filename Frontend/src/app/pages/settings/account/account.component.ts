import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder, Validators, NgForm, } from '@angular/forms';
import { accountSettingsForm } from 'src/app/utils/interfaces/settings.model';
import { ApiService } from 'src/app/utils/services/api.service';

@Component({
  selector: 'change-account-settings',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})

export class AccountComponent implements OnInit {
  @Input() input : Array<accountSettingsForm>;
  @Input() onSubmit: "password" | "email";
  @ViewChild('formVC', {static: false}) myForm: NgForm;

  form: FormGroup;

  constructor(private formBuilder: FormBuilder,
    public api: ApiService) {
    this.form = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    
  }

  submit() {
    if (this.form.valid) {
      if(this.onSubmit === "password"){

      }
      else if(this.onSubmit === "email"){
        this.api.changeEmail("", "");
      }
    }
  }

  reset(){
    this.form.reset();
    this.myForm.resetForm();
  }

}
