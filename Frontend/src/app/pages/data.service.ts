import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { webUrl } from "./../../assets/ts/globals";

export interface loginData {
  email: string;
  password: string;
}

@Injectable({
  providedIn: 'root'
})

export class DataService {

  constructor() { 

  }
}
