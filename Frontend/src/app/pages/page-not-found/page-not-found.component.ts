import { Component, OnInit, AfterViewInit } from '@angular/core';

import * as $ from "jquery";

@Component({
  selector: 'app-page-not-found',
  templateUrl: './page-not-found.component.html',
  styleUrls: ['./page-not-found.component.scss']
})
export class PageNotFoundComponent implements OnInit, AfterViewInit {

  constructor() { }

  ngOnInit() {
    
  }

  ngAfterViewInit(){
    $("body").html();
  }

}
