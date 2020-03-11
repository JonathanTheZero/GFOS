import { Component, OnInit, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators, } from '@angular/forms';
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
      this.validate()();
    }
  }

  reset(){
    this.form.reset();
  }

  private validate(): () => void {
    if(this.onSubmit === "password"){
      return () => {
      };
    }
    else if(this.onSubmit === "email") {
      return () => {
        this.api.changeEmail("", "");
      };
    }
  }

}
