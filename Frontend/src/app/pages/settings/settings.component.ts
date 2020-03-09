import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { accountSettingsForm } from 'src/app/utils/interfaces/settings.model';
import { ApiService } from 'src/app/utils/services/api.service';

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

  constructor(private titleService: Title,
    public api: ApiService) { }

  ngOnInit(): void {
    this.titleService.setTitle("Einstellungen");
  }

  public validateEmail(): void{
    
  }

  public validatePassword() : void {

  }

}
