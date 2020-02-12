import { Injectable } from '@angular/core';
import * as $ from "jquery";

@Injectable({
  providedIn: 'root'
})

export class UtilsService {

  constructor() {

  }

  public loggedIn() : boolean {
    if(sessionStorage.getItem("loggedin")){
      return true;
    }
    return false;
  }

  public logIn(): boolean {
    const id = this.generateId();
    sessionStorage.setItem("loggedin", id);
    $.getJSON(`${internal.apiUrl}/auth?id=${id}`, (data: request.authAnswer) => {
      if(data.error){
        console.log("Error, answer was: " + data.error);
      }
      else {
        console.log("Success: " + data.message);
      }
    });

    if(this.loggedIn()){
      return true;
    }
    return false;
  }
  
  private generateId (len=20) : string {
    const dec2hex = (dec) =>  ('0' + dec.toString(16)).substr(-2);
    var arr = new Uint8Array(len / 2);
    window.crypto.getRandomValues(arr);
    return Array.from(arr, dec2hex).join('');
  }
}