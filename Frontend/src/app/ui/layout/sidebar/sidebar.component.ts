import { Component, OnInit } from '@angular/core';
import { routerLinks } from 'src/app/utils/interfaces/sidebar.model';
import { DataService } from 'src/app/utils/services/data.service';
import { Mitarbeiter, Arbeitsgruppe } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  user: Mitarbeiter;
  groups: Arbeitsgruppe[];

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
    }
  ];

  constructor(public dataService: DataService,
    public api: ApiService){

  }

  ngOnInit() {
    this.dataService.getUser(true).subscribe(u => this.user = u);
    if(this.user) 
      this.api.getGroupsFromUser(this.user.personalnummer)
        .then(g => this.groups = g as Arbeitsgruppe[]);
  }

  public changeIcon(index: number, toggle: boolean): void {
    if(!this.sidebarLinks[index].iconWhenClosed) return;
    //swap the two values -> change of animation in the sidebar
    [this.sidebarLinks[index].icon, this.sidebarLinks[index].iconWhenClosed] = 
      [this.sidebarLinks[index].iconWhenClosed, this.sidebarLinks[index].icon];
  }
}
