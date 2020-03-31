import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { accountSettingsForm, options } from 'src/app/utils/interfaces/settings.model';
import { ApiService } from 'src/app/utils/services/api.service';
import { DataService } from 'src/app/utils/services/data.service';

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
      formControlName: "email",
      error: {
        email: "Bitte geben Sie eine gültige E-Mail Adresse ein",
        required: "Bitte geben Sie eine E-Mail Adresse an"
      }
    },
    {
      label: "Passwort",
      type: "password",
      formControlName: "password",
      error: {
        required: "Bitte geben Sie Ihr Passwort zur Bestätigung ein"
      }
    }
  ]

  public readonly changePassword: accountSettingsForm[] = [
    {
      label: "Altes Passwort",
      type: "password",
      formControlName: "password",
      error: {
        required: "Bitte geben Sie Ihr aktuelles Passwort ein",
      }
    },
    {
      label: "Neues Passwort",
      type: "password",
      formControlName: "newPassword",
      error: {
        minLength: "Ihr neues Passwort muss mindestens 8 Zeichen lang sein",
        required: "Bitte geben Sie ein neues Passwort an"
      }
    },
    {
      label: "Neues Passwort bestätigen",
      type: "password",
      formControlName: "confirmNewPassword",
      error: {
        required: "Bitte bestätigen Sie Ihre Eingabe",
        minLength: "Ihr neues Passwort muss mindestens 8 Zeichen lang sein"
      }
    }
  ]

  //data to fill the select, again making advantage of Angulars dynamic component features
  public readonly warningOptions: options = [
    {
      value: 5,
      text: "5 Minuten"
    },
    {
      value: 10,
      text: "10 Minuten (Standard)"
    },
    {
      value: 15,
      text: "15 Minuten"
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

    //allows to display current settings
    this.dataService.idleCounter.subscribe(x => this.currentSettings.idle = x);
    this.dataService.timeoutCounter.subscribe(x => this.currentSettings.logOut = x);
  }

}
