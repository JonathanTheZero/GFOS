import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {
  
  constructor(private titleService: Title){ }

  ngOnInit(): void {
    this.titleService.setTitle("Einstellungen");
  }

}
