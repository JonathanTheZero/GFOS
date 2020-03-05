import { Component, OnInit, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { accountSettingsForm } from 'src/app/utils/interfaces/settings.model';

@Component({
  selector: 'change-account-settings',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit {
  @Input() input : Array<accountSettingsForm>;
  @Input() onSubmit: () => void;
  form: FormGroup;

  constructor(private formBuilder: FormBuilder) {
    this.form = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    
  }

  submit() {
    if (this.form.valid) {
      this.onSubmit();
    }
  }

}
