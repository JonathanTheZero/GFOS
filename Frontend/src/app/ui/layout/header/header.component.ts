import { Component, OnInit } from '@angular/core';
import { DataService } from 'src/app/utils/services/data.service';
import { Mitarbeiter, apiAnswer } from 'src/app/utils/interfaces/default.model';
import { ApiService } from 'src/app/utils/services/api.service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})

export class HeaderComponent implements OnInit {

  //links allowing dynamic binding
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

  public user: Mitarbeiter;

  constructor(private dataService: DataService,
    private api: ApiService,
    private router: Router) { }

  ngOnInit(): void {
    this.dataService.getUser(true).subscribe(u => this.user = u);
  }

  //log out for the user...
  public logout(): void {
    this.api.logout().then((answer: apiAnswer) => {
      if (answer.erfolg) {
        Swal.fire("", "Sie wurden erfolgreich ausgeloggt", "success");
      }
      else {
        Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + answer.fehler, "error");
      }
      this.router.navigate(['/login']);
      setTimeout(window.location.reload.bind(window.location), 3000);
    });
  }

}
