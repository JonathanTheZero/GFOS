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

  public logIn(): internal.success | internal.error {
    const id = this.generateId(20);
    sessionStorage.setItem("loggedin", id);
    $.getJSON(`${internal.apiUrl}/auth?id=${id}`, (data: answer.success | answer.error) => {
      if(answer.instanceOfSuccess(data)){
        console.log("Success");
      }
      else if(answer.instanceOfError(data)){
        console.log("Error: " + data.reason);
      }
      else {
        console.log("Error: Unknown answer");
      }
    });

    if(this.loggedIn()){
      return {
        message: "Session created successfully"
      };
    }
    return {
      reason: "Something went wrong" 
    };
  }
  
  private generateId (len=40) : string {
    const dec2hex = (dec) =>  ('0' + dec.toString(16)).substr(-2);
    var arr = new Uint8Array(len / 2);
    window.crypto.getRandomValues(arr);
    return Array.from(arr, dec2hex).join('');
  }
}
