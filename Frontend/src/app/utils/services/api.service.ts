import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { apiAnswer, Mitarbeiter, Arbeitsgruppe, MitarbeiterAtribut } from "../interfaces/default.model";
import { environment } from "src/environments/environment";
import { DataService } from "./data.service";
import { employeeSamples, groupSamples } from '../mock.data';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: "root"
})

/**
 * @author Jonathan
 */
export class ApiService {
  private readonly url: string = "http://localhost:4200/Backend/api";

  private httpOptions = {
    headers: new HttpHeaders({
      "Content-Type": "application/json"
    })
  };

  constructor(private http: HttpClient, private dataService: DataService) { }

  getEmployeeSamples(): Observable<Array<Mitarbeiter>> {
    return of(employeeSamples);
  }

  /*
   * All methods are using API requests (using Angulars HttpModule)
   * the answer has a specified JSON format (apiAnswer from ../interfaces/default.model.ts)
   * the data service handels saving the auth token and the user data
   * 
   * Note: 
   * This service uses Promises and async/await instead of Observables
   * this has been done, because .then chaining is much easier if the user needs
   * to react to the returned values by the API and it is not expected that these values change
   * while the user interacts with the site (where Observables would be more useful)
   */

  /**
   * This methods registers a new user
   * @param name the last name of the new user
   * @param vn the first name of the new user
   * @param email the email of the new user
   * @param pw the password of the new user
   * @param rk the access-rights of the new user
   * @param abt the divison of the new user
   * @param ve the agent of the new user
   * @returns a Promsie that holds the answer of the API as object
   */
  public async registerNewUser(
    name: string,
    vn: string,
    email: string,
    pw: string,
    rk: string,
    abt: string,
    ve: string
  ): Promise<apiAnswer> {
    try {
      return await this.http
        .get<apiAnswer>(`${this.url}/mitarbeiter/add:auth=${this.dataService.getAuth()}&n=${name}&vn=${vn}&em=${email}&pw=${pw}&rk=${rk}&ab=${abt}&ve=${ve}`)
        .toPromise();
    }
    catch {
      return {
        fehler: "Es konnte keine Verbindung zum Server hergestellt werden"
      }
    }
  }

  /**
   * sends a login request to the server
   * @param pn the id of the user
   * @param pw the pw of the user
   * @returns a Promsie that holds the answer of the API as object
   */
  public async login(pn: string, pw: string): Promise<apiAnswer> {
    try {
      const auth: string = this.generateAuthToken();
      //keeping the user up-to-date
      this.http
        .get<apiAnswer>(`${this.url}/login:auth=${auth}&pn=${pn}&pw=${pw}`)
        .subscribe(a => this.dataService.setUser(a.data as Mitarbeiter));

      let a = await this.http
        .get<apiAnswer>(`${this.url}/login:auth=${auth}&pn=${pn}&pw=${pw}`)
        .toPromise<apiAnswer>();

      this.dataService.setAuth(auth);
      this.logoutBeacon();
      return a;
    }
    catch {
      return {
        fehler: "Es konnte keine Verbindung zum Server hergestellt werden"
      }
    }
  }

  /**
   * Methods logs the user out and deletes the current authtoken
   * @returns a Promsie that holds the answer of the API as object
   */
  public async logout(): Promise<apiAnswer> {
    try {
      var answer = await this.http
        .get<apiAnswer>(`${this.url}/logout:auth=${this.dataService.getAuth()}`)
        .toPromise();
      this.dataService.setAuth(undefined);
      this.dataService.setUser(undefined);
      return answer;
    }
    catch {
      return {
        fehler: "Es konnte keine Verbindung zum Server hergestellt werden"
      }
    }
  }

  /**
   * Schedules a beacon that should be fired when the user logs off
   * @returns whether the scheduling was successful or not
   */
  public logoutBeacon(): boolean {
    return navigator.sendBeacon(`${this.url}/logout:auth=${this.dataService.getAuth()}`, this.dataService.getAuth());
  }

  /**
   * Sends a request to alter the status
   * @param newStatus the new status that should be set
   * @param pn the ID of the user, if not given the one of the current user is used
   * @returns A Promsie that holds the answer of the API
   */
  public async alterStatus(newStatus: string, pn=this.dataService.getUser().personalnummer): Promise<apiAnswer> {
    try {
      return await this.http
        .get<apiAnswer>(`${this.url}/mitarbeiter/alter:auth=${this.dataService.getAuth()}&pn=${pn}&s=${newStatus}`)
        .toPromise();
    }
    catch {
      return {
        fehler: "Es konnte keine Verbindung zum Server hergestellt werden"
      };
    }
  }

  /**
   * Sends a request to change the reachable status of a user
   * @param r the new reachable value
   * @param pn the ID of the user, if not given the one of the current user is used
   * @returns a Promise that holds the answer of the API
   */
  public async alterReachable(r: boolean, pn=this.dataService.getUser().personalnummer): Promise<apiAnswer> {
    try {
      return await this.http
        .get<apiAnswer>(`${this.url}/mitarbeiter/alter:auth=${this.dataService.getAuth()}&pn=${pn}&er=${Number(r)}`)
        .toPromise();
    }
    catch {
      return {
        fehler: "Es konnte keine Verbindung zum Server hergestellt werden"
      };
    }
  }

  /**
   * sends a request to changte the email of the user
   * @param pw the password of the user
   * @param email the new email of the user
   * @returns a Promsie that holds the answer of the API as object
   */
  public async changeEmail(pw: string, email: string): Promise<apiAnswer> {
    try {
      return await this.http
        .get<apiAnswer>(`${this.url}/mitarbeiter/alter:auth=${this.dataService.getAuth()}&pn=${this.dataService.getUser().personalnummer}&em=${email}`)
        .toPromise();
    }
    catch {
      return {
        fehler: "Es konnte keine Verbindung zum Server hergestellt werden"
      }
    }
  }

  /**
   * Sends a request to change the password of the user
   * @param pw the old password of the user
   * @param newPw the new password of the user
   * @returns a Promsie that holds the answer of the API as object
   */
  public async changePassword(pw: string, newPw: string): Promise<apiAnswer> {
    try {
      return await this.http
        .get<apiAnswer>(`${this.url}/mitarbeiter/alterPassword:auth=${this.dataService.getAuth()}&old=${pw}&new=${newPw}`)
        .toPromise();
    }
    catch {
      return {
        fehler: "Es konnte keine Verbindung zum Server hergestellt werden"
      }
    }
  }

  /**
  * Returns the data of a specific user
  * @param pn the ID of the requested employee
  * @returns the stats as Promise that holds the API answer Object
  */
  public async getUser(pn: string | number): Promise<Mitarbeiter | apiAnswer> {
    try {
      return await this.http
        .get<Mitarbeiter>(`${this.url}/mitarbeiter/get:auth=${this.dataService.getAuth()}&pn=${pn}`)
        .toPromise();
    }
    catch {
      if(!environment.production) return employeeSamples[Math.floor((employeeSamples.length - 1) * Math.random())];
      return {
        fehler: "Es konnte keine Verbindung zum Server hergestellt werden"
      };
    }
  }

  /**
   * Sends a request to create a new group
   * @param name Name of the new Group
   * @param pn optional parameter who should be the admin, if no is giving the current user is used
   * @returns a Promise that holds the API answer Object
   */
  public async addGroup(name: string, pn?: string | number): Promise<apiAnswer | Arbeitsgruppe> {
    try {
      return await this.http
        .get<apiAnswer>(`${this.url}/arbeitsgruppen/add:auth=${this.dataService.getAuth()}&name=${name}&pn=${pn || this.dataService.getUser().personalnummer}`)
        .toPromise();
    }
    catch {
      if(!environment.production) return groupSamples[0];
      return {
        fehler: "Es konnte keine Verbindung zum Server hergestellt werden"
      }
    }
  }

  /**
   * Sends a request to remove a sepcific user from a specific group
   * @param pn the ID of the user that should get removed
   * @param groupID the ID of the group from which the user should get removed
   * @returns a Promise that holds the API answer Object
   */
  public async removeFromGroup(pn: number | string, groupID: string): Promise<apiAnswer> {
    try {
      return await this.http
        .get<apiAnswer>(`${this.url}/arbeitsgruppen/removeMitarbeiter:auth=${this.dataService.getAuth()}&pn=${pn}&arbeitsgruppenID=${groupID}`)
        .toPromise();
    }
    catch {
      return {
        fehler: "Es konnte keine Verbindung zum Server hergestellt werden"
      }
    }
  }

  /**
   * Sends a request to add a specific user to a specific group
   * @param pn the ID of the user that should get added
   * @param groupID the ID of the group to which the user should get added
   * @returns a Promies that holds the API answer Object
   */
  public async addToGroup(pn: number | string, groupID: string): Promise<apiAnswer> {
    try {
      return await this.http
        .get<apiAnswer>(`${this.url}/arbeitsgruppen/addMitarbeiter:auth=${this.dataService.getAuth()}&pn=${pn}&arbeitsgruppe=${groupID}`)
        .toPromise();
    }
    catch {
      return {
        fehler: "Es konnte keine Verbindung zum Server hergestellt werden"
      }
    }
  }

  /**
   * Gets a group via it's name
   * @param name the name of the group
   * @returns a Promise that holds the answer given back by the server
   */
  public async getGroupByName(name: string): Promise<Arbeitsgruppe | apiAnswer> {
    try {
      return await this.http
        .get<Arbeitsgruppe>(`${this.url}/arbeitsgruppen/getFromBezeichnung:auth=${this.dataService.getAuth()}&name=${name}`)
        .toPromise();
    }
    catch {
      if(!environment.production) return groupSamples[0];
      Swal.fire("Fehler", "Es konnte keine Verbindung zum Server hergestellt werden", "error");
      return undefined;
    }
  }

  /**
   * Requests all groups from the server
   * @returns an Array including all current groups from the server
   */
  public async getAllGroups(): Promise<Array<Arbeitsgruppe>> | undefined {
    try {
      return await this.http
        .get<Array<Arbeitsgruppe>>(`${this.url}/arbeitsgruppen/getAll:auth=${this.dataService.getAuth()}`)
        .toPromise();
    }
    catch {
      if(!environment.production) return groupSamples;
      Swal.fire("Fehler", "Es konnte keine Verbindung zum Server hergestellt werden", "error");
      return undefined;
    }
  }

  /**
   * Sends a request to fetch all groups a specific user is in
   * @param pn the ID of the user whose groups should be requested
   * @returns a Promsie that holds an Array of all groups or an error
   */
  public async getGroupsFromUser(pn=this.dataService.getUser().personalnummer): Promise<Array<Arbeitsgruppe> | apiAnswer> {
    try {
      return await this.http
        .get<Arbeitsgruppe[]>(`${this.url}/arbeitsgruppen/getAllFromMitarbeiter:auth=${this.dataService.getAuth()}&pn=${pn}`)
        .toPromise();
    }
    catch {
      if(!environment.production) return groupSamples;
      return {
        fehler: "Es konnte keine Verbindung zum Server hergestellt werden"
      };
    }
  }

  /**
   * Get a group via it's ID
   * @param groupID the ID of the group
   * @returns either a Promise that holds the group object or an error
   */
  public async getGroupFromID(groupID: string): Promise<Arbeitsgruppe | apiAnswer> {
    try {
      return await this.http
        .get<Arbeitsgruppe>(`${this.url}/arbeitsgruppen/getFromID:auth=${this.dataService.getAuth()}&ID=${groupID}`)
        .toPromise();
    }
    catch {
      if(!environment.production) return groupSamples[1];
      return {
        fehler: "Es konnte keine Verbindung zum Server hergestellt werden"
      }
    }
  }

  /**
   * This methods allows better handling for requesting all users from a specific group
   * @param groupID the ID of the group whose users should be returned
   * @returns a Promise that holds a tuple which has the group leader as first
   *  object and an Array of users as the second one or an error message
   */
  public async getAllUsersFromGroup(groupID: string): Promise<[Mitarbeiter, Array<Mitarbeiter>] | apiAnswer> {
    try {
      //decclaring the tuple array
      let arr: [Mitarbeiter, Array<Mitarbeiter>] = [undefined, []]; 
      //requesting the grup
      const group: Arbeitsgruppe = await this.getGroupFromID(groupID) as Arbeitsgruppe; 
      //push the leader
      arr[0] = await this.getUser(group.leiter) as Mitarbeiter;
      //promise array and pushing all user promises to it
      const promises: Array<Promise<Mitarbeiter>> = [];
      for(let m of group.mitglieder){
        promises.push(this.getUser(m) as Promise<Mitarbeiter>);
      }
      //resolving all promises, applying to tuple, return
      arr[1] = await Promise.all(promises);
      return arr; 
    }
    catch {
      if(!environment.production) return [employeeSamples[0], employeeSamples];
      return {
        fehler: "Es konnte keine Verbindung zum Server hergestellt werden"
      };
    }
  }

  /*
   * private methods, names are self explaining
   */

  /**
   * Generates a new auth token
   * @returns a new auth token
   */
  private generateAuthToken(): string {
    const dec2hex = (dec: number) => ("0" + dec.toString(16)).substr(-2); //convert decimal to hexadecimal
    var arr = new Uint8Array(24); //makes a length 12 auth token
    window.crypto.getRandomValues(arr);
    let x = Array.from(arr, dec2hex).join("");
    return x.substr(0, 12);
  }
}
