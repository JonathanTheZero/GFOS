import { Component, OnInit } from '@angular/core';
import { routerLinks } from 'src/app/utils/interfaces/sidebar.model';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  constructor() { }

  public readonly sidebarLinks : Array<routerLinks> = [
    {
      title: "Normal",
      icon: "user",
      links: [
        {
          link: "/dashboard",
          title: "Dashboard"
        },
        {
          link: "/settings",
          title: "Einstellungen"
        }
      ]
    },
  ];


  ngOnInit() {
  }
}
