import { Component, OnInit, ViewChild, ViewContainerRef, ComponentRef, ComponentFactoryResolver } from '@angular/core';
import { SidebarComponent } from '../sidebar/sidebar.component';

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
      link: ['/', 'dashboard'],
      label: 'Dashboard'
    },
    {
      link: ['/', 'posts'],
      label: 'Posts'
    },
    {
      link: ['/', 'todos'],
      label: 'Todos'
    },
    {
      link: ['/', 'users'],
      label: 'Users'
    },
  ];

  @ViewChild('container', { read: ViewContainerRef }) container: ViewContainerRef;

  // Keep track of list of generated components for removal purposes
  components: Array<ComponentRef<any>> = [];
  //declare component as variable so it can be used in the functions
  private sidebar: typeof SidebarComponent = SidebarComponent;
  private sidebarStatus: boolean = false; //wether the sidebar is expanded or not

  constructor(private componentFactoryResolver: ComponentFactoryResolver) { }

  ngOnInit() {
  }

  public toggleSidebar() {
    if (this.sidebarStatus) {
      // Create component dynamically... seriously this was hell to accomplish
      const componentFactory = this.componentFactoryResolver.resolveComponentFactory(this.sidebar);
      const component = this.container.createComponent(componentFactory);

      //set clarity attributes
      document.getElementById("outerSidebar").setAttribute("class", "sidenav");
      document.getElementsByTagName("app-sidebar")[0].setAttribute("id", "innerSidebarSelector")

      // Push the component so that we can keep track of which components are created
      this.components.push(component);
    }
    else {
      var p = document.getElementById("outerSidebar");
      p.removeChild(document.getElementById("innerSidebarSelector"));
      p.removeAttribute("class");
    }
    this.sidebarStatus = !this.sidebarStatus;
  }

}
