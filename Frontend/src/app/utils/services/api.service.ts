import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Todo } from '../interfaces/dashboard.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { todoSamples } from '../mock.data';
import { apiAnswer } from '../interfaces/default.model';

@Injectable({
  providedIn: 'root'
})

export class ApiService {
  private readonly url: string = "http://localhost:8080/award/api";

  private httpOptions = {
    headers: new HttpHeaders(
      {
        'Content-Type': 'application/json'
      }
    )
  };

  todoSamples: Todo[];

  constructor(private http: HttpClient) {
    this.todoSamples = todoSamples;
  }

  getTodoSamples(): Observable<Todo[]> {
    return of(this.todoSamples);
  }

  public registerNewUser(name: string, vn: string, email: string, pw: string): apiAnswer {
    var answer: apiAnswer;
    this.http.get<apiAnswer>(`${this.url}/mitarbeiter/add:auth=${this.generateAuthToken()}&n=${name}&vn=${vn}&em=${email}&pw=${pw}`).pipe(
      null,
      catchError(this.handleError<apiAnswer>(`Tried login with ${this.url}/mitarbeiter/add:auth=${this.generateAuthToken()}&n=${name}&vn=${vn}&em=${email}&pw=${pw})`))
    ).subscribe(x => answer = x);
    return answer;
  }

  private generateAuthToken(): string {
    const dec2hex = (dec: number) => ('0' + dec.toString(16)).substr(-2);
    var arr = new Uint8Array(40);
    window.crypto.getRandomValues(arr);
    return Array.from(arr, dec2hex).join('');
  }

  public logout(authToken: string): apiAnswer {
    var answer: apiAnswer;
    this.http.get<apiAnswer>(`${this.url}/logout:auth=${authToken}`).pipe(
      null,
      catchError(this.handleError<apiAnswer>(`Logout failed`))
    ).subscribe(x => answer = x);
    return answer;
  }

  private handleError<T>(operation = 'operation', result?: T): (error: any) => Observable<T> {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }
}


