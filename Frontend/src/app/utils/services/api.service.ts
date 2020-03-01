import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Todo } from '../interfaces/dashboard.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { todoSamples, employeeSamples } from '../mock.data';
import { apiAnswer, Mitarbeiter } from '../interfaces/default.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})

export class ApiService {
  private readonly url: string = "http://localhost:8080/award/api";

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) {
  }

  getTodoSamples(): Observable<Todo[]> {
    return of(todoSamples);
  }

  getEmployeeSamples(): Observable<Array<Mitarbeiter>> {
    return of(employeeSamples);
  }

  public getCurrentUser(): Observable<Mitarbeiter> {
    if(!environment.production) return of(employeeSamples[0]);
    return this.http.get<Mitarbeiter>(`${this.url}/`).pipe(
      null, null
    );
  }

  public registerNewUser(name: string, vn: string, email: string, pw: string): apiAnswer {
    var answer: apiAnswer;
    const auth: string = this.generateAuthToken();
    this.http.get<apiAnswer>(`${this.url}/mitarbeiter/add:auth=${auth}&n=${name}&vn=${vn}&em=${email}&pw=${pw}`).subscribe(x => answer = x);
    sessionStorage.setItem("currentUser", auth);
    return answer;
  }

  public login(pn: string, pw: string): apiAnswer {
    var answer: apiAnswer;
    const auth: string = this.generateAuthToken();
    this.http.get<apiAnswer>(`${this.url}/login:auth=${auth}&pn=${pn}&pw=${pw}`).subscribe(x => answer = x);
    sessionStorage.setItem("currentUser", auth);
    return answer;
  }

  public logout(authToken: string): apiAnswer {
    var answer: apiAnswer;
    this.http.get<apiAnswer>(`${this.url}/logout:auth=${authToken}`).subscribe(x => answer = x);
    sessionStorage.removeItem("currentUser");
    return answer;
  }

  private generateAuthToken(): string {
    const dec2hex = (dec: number) => ('0' + dec.toString(16)).substr(-2);
    var arr = new Uint8Array(24); //makes a length 12 auth token
    window.crypto.getRandomValues(arr);
    return Array.from(arr, dec2hex).join('');
  }

  private handleError<T>(operation = 'operation', result?: T): (error: any) => Observable<T> {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }
}


