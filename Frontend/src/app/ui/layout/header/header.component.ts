import { Component, OnInit} from '@angular/core';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {
  public readonly headerLinks = [
    {
      link: ['/', 'dashboard'],
      icon: 'home'
    },
    {
      link: ['/', 'settings'],
      icon: 'cog'
    },
  ];

  constructor() { }

  ngOnInit() {
  }

}
