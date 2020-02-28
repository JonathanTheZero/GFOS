import { Component, OnInit } from '@angular/core';

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

  public readonly subLinks = [
    { 
      link : ['/', 'dashboard'], 
      label: 'Dashboard' 
    },
    { 
      link : ['/', 'posts'], 
      label: 'Posts' 
    },
    { 
      link : ['/', 'todos'], 
      label: 'Todos'
    },
    { 
      link : ['/', 'users'], 
      label: 'Users' 
    },
  ];

  constructor() { }

  ngOnInit() {
  }

}
