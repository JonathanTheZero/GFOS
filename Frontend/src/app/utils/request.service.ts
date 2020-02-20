import { Injectable } from '@angular/core';
import { getJSON } from 'jquery';
import { Mitarbeiter } from './interfaces';

@Injectable({
  providedIn: 'root'
})

const webUrl = "http://localhost:8080/award/api";

export class RequestService {

  constructor() { }

  public generateNewUser(auth: string | number, newUser: Mitarbeiter) {
    getJSON(`${webUrl}/mitarbeiter/add:auth=${auth}&pn=${newUser.personalnummer}&n=${newUser.name}&vn=${newUser.vorname}&er=0&ak=20&em=n.s@e.de&s=Abwesend&rk=Admin&ab=IT-Sicherheit&ve=000000000000`, 
      data => {

      });
    return true;
  }

}
