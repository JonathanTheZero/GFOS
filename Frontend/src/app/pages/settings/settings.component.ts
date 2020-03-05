import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { accountSettingsForm } from 'src/app/utils/interfaces/settings.model';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {

  public readonly changeEmail: accountSettingsForm[] = [
    {
      label: "E-Mail",
      type: "email",
      error: {
        message: "Bitte geben Sie eine gültige E-Mail Adresse an",
        type: "required"
      }
    }
  ]

  constructor(private titleService: Title) { }

  ngOnInit(): void {
    this.titleService.setTitle("Einstellungen");
  }

}
