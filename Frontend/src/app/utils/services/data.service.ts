import { Injectable } from '@angular/core';
import { Mitarbeiter } from '../interfaces/default.model';
import { environment } from 'src/environments/environment';
import { employeeSamples } from '../mock.data';

@Injectable({
  providedIn: 'root'
})

/**
 * This service explicity <b>doesn't</b> work with the API
 * it only serves static data that has been a result of previous API requests to the User
 */
export class DataService {

  constructor() { }

  public getUser(): Mitarbeiter {
    if(!environment.production) return employeeSamples[0];
    return JSON.parse(sessionStorage.getItem("currentUserData")) as Mitarbeiter;
  }
}
