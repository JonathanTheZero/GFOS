import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { apiAnswer, Mitarbeiter, apiStats } from "../interfaces/default.model";
import { environment } from "src/environments/environment";
import { DataService } from "./data.service";
import { employeeSamples } from '../mock.data';

@Injectable({
  providedIn: "root"
})

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
    return await this.http
      .get<apiAnswer>(`${this.url}/mitarbeiter/add:auth=${this.dataService.getAuth()}&n=${name}&vn=${vn}&em=${email}&pw=${pw}&rk=${rk}&ab=${abt}&ve=${ve}`)
      .toPromise();
  }

  /**
   * sends a login request to the server
   * @param pn the id of the user
   * @param pw the pw of the user
   * @returns a Promsie that holds the answer of the API as object
   */
  public async login(pn: string, pw: string): Promise<apiAnswer> {
    const auth: string = this.generateAuthToken();
    //keeping the user up-to-date
    this.http
      .get<apiAnswer>(`${this.url}/login:auth=${auth}&pn=${pn}&pw=${pw}`)
      .subscribe(a => this.dataService.setUser(a.data));

    let a = await this.http
      .get<apiAnswer>(`${this.url}/login:auth=${auth}&pn=${pn}&pw=${pw}`)
      .toPromise<apiAnswer>();

      console.log(JSON.stringify(a));

    this.dataService.setAuth(auth);
    return a;
  }

  /**
   * Methods logs the user out and deletes the current authtoken
   * @returns a Promsie that holds the answer of the API as object
   */
  public async logout(): Promise<apiAnswer> {
    var answer = await this.http
      .get<apiAnswer>(`${this.url}/logout:auth=${this.dataService.getAuth()}`)
      .toPromise();
    this.dataService.setAuth(undefined);
    this.dataService.setUser(undefined);
    return answer;
  }

  /**
   * sends a request to changte the email of the user
   * @param pw the password of the user
   * @param email the new email of the user
   * @returns a Promsie that holds the answer of the API as object
   */
  public async changeEmail(pw: string, email: string): Promise<apiAnswer> {
    return await this.http
      .get<apiAnswer>(`${this.url}/mitarbeiter/alter:auth=${this.dataService.getAuth()}&pn=${this.dataService.getUser().personalnummer}&em=${email}`)
      .toPromise();
  }

  /**
   * Sends a request to change the password of the user
   * @param pw the old password of the user
   * @param newPw the new password of the user
   * @returns a Promsie that holds the answer of the API as object
   */
  public async changePassword(pw: string, newPw: string): Promise<apiAnswer> {
    return await this.http
      .get<apiAnswer>(`${this.url}/mitarbeiter/alterPassword:auth=${this.dataService.getAuth()}&old=${pw}&new=${newPw}`)
      .toPromise();
  }

  /**
   * Returns the current stats about the system (if wished as Observble)
   * @returns the current stats hold by a Promise
   */
  public async getStats(): Promise<apiStats> {
    return await this.http
      .get<apiStats>(`${this.url}/stats`)
      .toPromise();
  }

  /**
  * Returns the data of a specific user
  * @param pn the ID of the requested employee
  * @returns the stats as Promise that holds the API answer Object
  */
  public async getUser(pn: string | number): Promise<Mitarbeiter> {
    return await this.http
      .get<Mitarbeiter>(`${this.url}/mitarbeiter/get:auth=${this.dataService.getAuth()}&pn=${pn}`)
      .toPromise();
  }

  /**
   * Sends a request to create a new group
   * @param name Name of the new Group
   * @param pn optional parameter who should be the admin, if no is giving the current user is used
   * @returns a Promise that holds the API answer Object
   */
  public async addGroup(name: string, pn?: string | number): Promise<apiAnswer> {
    return await this.http
      .get<apiAnswer>(`${this.url}/arbeitsgruppe/add:auth=${this.dataService.getAuth()}&name=${name}&pn=${pn || this.dataService.getUser().personalnummer}`)
      .toPromise();
  }

  /**
   * Sends a request to remove a sepcific user from a specific group
   * @param pn the ID of the user that should get removed
   * @param groupID the ID of the group from which the user should get removed
   * @returns a Promise that holds the API answer Object
   */
  public async removeFromGroup(pn: number | string, groupID: string): Promise<apiAnswer> {
    return await this.http
      .get<apiAnswer>(`${this.url}/arbeitsgruppe/removeMitarbeiter:auth=${this.dataService.getAuth()}&pn=${pn}&arbeitsgruppenID=${groupID}`)
      .toPromise();
  }

  /**
   * Sends a request to add a specific user to a specific group
   * @param pn the ID of the user that should get added
   * @param groupID the ID of the group to which the user should get added
   * @returns a Promies that holds the API answer Object
   */
  public async addToGroup(pn: number | string, groupID: string): Promise<apiAnswer> {
    return await this.http
      .get<apiAnswer>(`${this.url}/arbeitsgruppe/addMitarbeiter:auth=${this.dataService.getAuth()}&pn=${pn}&arbeitsgruppe=${groupID}`)
      .toPromise();
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
