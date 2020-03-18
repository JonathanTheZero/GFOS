import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { Todo } from "../interfaces/dashboard.model";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { todoSamples, employeeSamples } from "../mock.data";
import { apiAnswer, Mitarbeiter, apiStats } from "../interfaces/default.model";
import { environment } from "src/environments/environment";
import { DataService } from "./data.service";

@Injectable({
  providedIn: "root"
})

export class ApiService {
  private readonly url: string = "http://localhost:8080/Backend/api";

  private httpOptions = {
    headers: new HttpHeaders({ "Content-Type": "application/json" })
  };

  constructor(private http: HttpClient, private dataService: DataService) { }

  /**
   * Samples for local testing only
   */
  getTodoSamples(): Observable<Array<Todo>> {
    return of(todoSamples);
  }

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
    const auth: string = this.generateAuthToken();
    let a = await this.http
      .get<apiAnswer>(`${this.url}/mitarbeiter/add:auth=${auth}&n=${name}&vn=${vn}&em=${email}&pw=${pw}&rk=${rk}&ab=${abt}&ve=${ve}`)
      .toPromise();
    this.dataService.setAuth(auth);
    return a;
  }

  /**
   * sends a login request to the server
   * @param pn the id of the user
   * @param pw the pw of the user
   * @returns a Promsie that holds the answer of the API as object
   */
  public async login(pn: string, pw: string): Promise<apiAnswer> {
    const auth: string = this.generateAuthToken();
    let a = await this.http
      .get<apiAnswer>(`${this.url}/login:auth=${auth}&pn=${pn}&pw=${pw}`)
      .toPromise<apiAnswer>();
    this.dataService.setAuth(auth);
    this.dataService.setUser(a.data);
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
      .get<apiAnswer>(``)
      .toPromise();
  }

  /**
   * Sends a request to change the password of the user
   * @param pw the old password of the user
   * @param newPw the new password of the user
   * @returns a Promsie that holds the answer of the API as object
   */
  public async changePassword(pw: string, newPw: string): Promise<apiAnswer> {
    return await this.http.get<apiAnswer>(`/alter:auth=${this.dataService.getAuth()}&pw=${newPw}`).toPromise();
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
    return Array.from(arr, dec2hex).join("");
  }
}
