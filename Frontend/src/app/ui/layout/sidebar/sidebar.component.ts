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

  public sidebarLinks : Array<routerLinks> = [
    {
      title: "Allgemein",
      icon: "folder-open",
      iconWhenClosed: "folder",
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
        },
        {
          link: "/employee/me",
          title: "Mein Profil",
          icon: "user"
        }
      ]
    },
    {
      title: "Meine Gruppen",
      icon: "",
      links: [
        {
          link: "/group/x",
          title: "..",
          icon: ""
        }
      ]
    }
  ];

  constructor(public dataService: DataService){

  }

  ngOnInit() {
    this.data = this.dataService.getUser();
  }

  public changeIcon(index: number, toggle: boolean): void {
    if(!this.sidebarLinks[index].iconWhenClosed) return;
    //swap the two values -> change of animation in the sidebar
    [this.sidebarLinks[index].icon, this.sidebarLinks[index].iconWhenClosed] = 
      [this.sidebarLinks[index].iconWhenClosed, this.sidebarLinks[index].icon];
  }
}
