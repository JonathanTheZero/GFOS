import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { catchError, map, tap } from "rxjs/operators";
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

  /**
   * All methods are using API requests (using Angulars HttpModule)
   * the answer has a specified JSON format (apiAnswer from ../interfaces/default.model.ts)
   * the data service handels saving the auth token and the user data
   * some methods are available either as Observable or as normal object
   * in order to provide live updates via subscribing
   */
  public registerNewUser(
    name: string,
    vn: string,
    email: string,
    pw: string,
    rk: string,
    abt: string,
    ve: string
  ): apiAnswer {
    var answer: apiAnswer;
    const auth: string = this.generateAuthToken();
    this.http
      .get<apiAnswer>(`${this.url}/mitarbeiter/add:auth=${auth}&n=${name}&vn=${vn}&em=${email}&pw=${pw}&rk=${rk}&ab=${abt}&ve=${ve}`)
      .subscribe(x => (answer = x))
      .unsubscribe();
    this.dataService.setAuth(auth);
    return answer;
  }

  public login(pn: string, pw: string): apiAnswer {
    var answer: apiAnswer;
    const auth: string = this.generateAuthToken();
    this.http
      .get<apiAnswer>(`${this.url}/login:auth=${auth}&pn=${pn}&pw=${pw}`)
      .subscribe(x => (answer = x))
      .unsubscribe();
    this.dataService.setAuth(auth);
    this.dataService.setUser(answer.data);
    return answer;
  }

  public logout(): apiAnswer {
    var answer: apiAnswer;
    this.http
      .get<apiAnswer>(`${this.url}/logout:auth=${this.dataService.getAuth()}`)
      .subscribe(x => (answer = x))
      .unsubscribe();
    this.dataService.setAuth(undefined);
    return answer;
  }

  public changeEmail(pw: string, email: string): Promise<apiAnswer> {
    const auth: string = this.dataService.getAuth();
    var answer: Promise<apiAnswer> = this.http
      .get<apiAnswer>(``)
      .toPromise();
    return answer;
  }

  public changePassword(pw: string, newPw: string): Promise<apiAnswer> {
    var answer: Promise<apiAnswer> = this.http.get<apiAnswer>(`/alter:auth=${this.dataService.getAuth()}&pw=${newPw}`).toPromise();
    return answer;
  }

  public getStats(asObservable: boolean): Observable<apiStats>;
  public getStats(): apiStats;
  public getStats(asObservable?: boolean): Observable<apiStats> | apiStats {
    if (asObservable) {
      return this.http.get<apiStats>(`${this.url}/stats`);
    }
    var answer: apiStats;
    this.http
      .get<apiStats>(`${this.url}/stats`)
      .subscribe(x => (answer = x))
      .unsubscribe();
    return answer;
  }

  public getUser(pn: string | number, asObservable: boolean): Observable<Mitarbeiter>
  public getUser(pn: string | number): Mitarbeiter;
  public getUser(pn: string | number, asObservable?: boolean): Observable<Mitarbeiter> | Mitarbeiter {
    if (asObservable) {
      if(!environment.production){
        if(pn === 1 || pn === "1"){
          return of(employeeSamples[0]);
        }
        return of(employeeSamples[1]);
      }
      return this.http
        .get<Mitarbeiter>(`${this.url}/mitarbeiter/get:auth=${this.dataService.getAuth()}&pn=${pn}`);
    }
    var emp: Mitarbeiter;
    this.http
      .get<Mitarbeiter>(`${this.url}/mitarbeiter/get:auth=${this.dataService.getAuth()}&pn=${pn}`)
      .subscribe(x => emp = x)
      .unsubscribe();
    return emp;
  }

  /**
   * private methods, names are self explaining
   */
  private generateAuthToken(): string {
    const dec2hex = (dec: number) => ("0" + dec.toString(16)).substr(-2); //convert decimal to hexadecimal
    var arr = new Uint8Array(24); //makes a length 12 auth token
    window.crypto.getRandomValues(arr);
    return Array.from(arr, dec2hex).join("");
  }
}
