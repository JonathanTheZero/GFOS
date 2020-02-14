import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  close(): void {
    var p = document.getElementById("sidebar");
    //document.getElementById("sidebar").style.height = "0";
    //document.getElementById("sidebar").style.width = "0";

    p.parentNode.removeChild(p);
  }

}
