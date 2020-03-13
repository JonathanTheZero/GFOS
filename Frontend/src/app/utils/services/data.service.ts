import { Injectable } from '@angular/core';
import { Mitarbeiter } from '../interfaces/default.model';
import { environment } from 'src/environments/environment';
import { employeeSamples } from '../mock.data';
import { Observable, of, Subject, BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

/**
 * This service explicity <b>doesn't</b> work with the API
 * it only serves static data that has been a result of previous API requests to the User
 */
export class DataService {

  private currentUser: Mitarbeiter;
  private auth: string;

  //initialize subjects using the already stored values
  public idleCounter: BehaviorSubject<string> = new BehaviorSubject<string>(this.getIdle());
  public timeoutCounter: BehaviorSubject<string> = new BehaviorSubject<string>(this.getTimeout());

  constructor() { }

  /**
   * basic getters and setters for methods
   * 
   * Note: some members will subscribe to the counters
   * since they can provide live updates
   * 
   * the methods below could've been implemented using js get/set syntax
   * but this looks cleaner in my opinion
   */
  public getUser(): Mitarbeiter {
    if (!environment.production) return employeeSamples[0];
    return this.currentUser;
  }

  public setAuth(auth: string): void {
    this.auth = auth;
  }

  public getAuth(): string {
    return this.auth;
  }

  public setUser(user: Mitarbeiter) {
    this.currentUser = user;
  }

  public getTimeout(): string {
    if(!localStorage.getItem('timeout')) localStorage.setItem('timeout', "2");
    return localStorage.getItem("timeout");
  }

  public setTimeout(t: string) {
    //using to notify subscriber components of the value that something has changed
    this.timeoutCounter.next(t);
    localStorage.setItem("timeout", t);
  }

  public getIdle(): string {
    if(!localStorage.getItem("idle")) localStorage.setItem('idle', "10");
    return localStorage.getItem("idle");
  }

  public setIdle(i : string){
    //using to notify subscriber components of the value that something has changed
    this.idleCounter.next(i);
    localStorage.setItem("idle", i);
  }
}
