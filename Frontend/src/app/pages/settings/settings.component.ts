import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { accountSettingsForm, options } from 'src/app/utils/interfaces/settings.model';
import { ApiService } from 'src/app/utils/services/api.service';
import { DataService } from 'src/app/utils/services/data.service';
import { Observable, of } from 'rxjs';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {

  //data to fill the account option -> dynamically adding components
  public readonly changeEmail: accountSettingsForm[] = [
    {
      label: "E-Mail",
      type: "email",
      error: {
        message: "Bitte geben Sie eine gültige E-Mail Adresse an",
        type: "required"
      }
    },
    {
      label: "Passwort",
      type: "password",
      error: {
        message: "Bitte geben Sie Ihr Passwort zur Bestätigung ein",
        type: "required"
      }
    }
  ]

  public readonly changePassword: accountSettingsForm[] = [
    {
      label: "Altes Passwort",
      type: "password",
      error: {
        message: "Bitte geben Sie Ihr Passwort zur Bestätigung ein",
        type: "required"
      }
    },
    {
      label: "Neues Passwort",
      type: "password",
      error: {
        message: "Bitte geben Sie ein neues Passwort ein",
        type: "required"
      }
    },
    {
      label: "Neues Passwort bestätigen",
      type: "password",
      error: {
        message: "Ihre Eingaben sind nicht identisch",
        type: "required"
      }
    }
  ]

  //data to fill the select, again making advantage of Angulars dynamic component features
  public readonly warningOptions: options = [
    {
      value: 2,
      text: "5 Mintuen"
    },
    {
      value: 10,
      text: "10 Mintuen (Standard)"
    },
    {
      value: 15,
      text: "15 Mintuen"
    },
  ];

  public readonly logoutOptions: options = [
    {
      value: 1,
      text: "1 Minute"
    },
    {
      value: 2,
      text: "2 Mintuen (Standard)"
    },
    {
      value: 5,
      text: "5 Mintuen"
    },
  ];

  public currentSettings;

  constructor(private titleService: Title,
    public api: ApiService,
    public dataService: DataService) { }

  ngOnInit(): void {
    this.currentSettings = {
      idle: this.dataService.getIdle(),
      logOut: this.dataService.getTimeout()
    };
    this.titleService.setTitle("Einstellungen");
    this.dataService.idleCounter.subscribe(x => this.currentSettings.idle = x);
    this.dataService.timeoutCounter.subscribe(x => this.currentSettings.logOut = x);
  }

  public validateEmail(): void {

  }

  public validatePassword(): void {

  }

}
