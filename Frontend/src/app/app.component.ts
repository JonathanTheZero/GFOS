import { Component, OnInit } from '@angular/core';
import { ApiService } from './utils/services/api.service';
import { apiAnswer } from './utils/interfaces/default.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})

export class AppComponent implements OnInit {
  title = 'Dashboard';

  constructor(private api: ApiService){

  }

  ngOnInit(): void {
    window.addEventListener("beforeunload", e => {
      //logout for the user if needed
      if(localStorage.getItem("currentUser")){
        const logout: apiAnswer = this.api.logout(localStorage.getItem("currentUser"));
        if(logout.erfolg){
          localStorage.removeItem("currentUser");
        }
        else {
          return logout.fehler;
        }
      }
      (e || window.event).returnValue = ""; //Gecko + IE
      return "";                            //Webkit, Safari, Chrome, Opera
    });
  }
}
