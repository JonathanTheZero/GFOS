import { Component, OnInit } from '@angular/core';
import { routerLinks } from 'src/app/utils/interfaces/sidebar.model';
import { DataService } from 'src/app/utils/services/data.service';
import { Mitarbeiter } from 'src/app/utils/interfaces/default.model';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  data: Mitarbeiter;

  public readonly sidebarLinks : Array<routerLinks> = [
    {
      title: "Normal",
      icon: "user",
      links: [
        {
          link: "/dashboard",
          title: "Dashboard",
          icon: "dashboard"
        },
        {
          link: "/settings",
          title: "Einstellungen",
          icon: "cog"
        }
      ]
    }
  ];

  constructor(public dataService: DataService){

  }

  ngOnInit() {
    this.data = this.dataService.getUser();
  }
}
