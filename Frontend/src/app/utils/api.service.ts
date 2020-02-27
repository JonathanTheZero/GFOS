import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Todo } from './interfaces/dashboard.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { todoSamples } from './mock.data';
import { apiSuccess, apiError } from './interfaces/default.model';

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

  private handleError<T>(operation = 'operation', result?: T): (error: any) => Observable<T> {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }

  public registerNewUser(name: string, vn: string, email: string, pw: string): void {
    this.http.get<apiSuccess | apiError>(`${this.url}/mitarbeiter/add:auth=${this.generateAuthToken()}&n=${name}&vn=${vn}&em=${email}&pw=${pw}`);
  }

  private generateAuthToken(): string {
    const dec2hex = (dec: number) => ('0' + dec.toString(16)).substr(-2);
    var arr = new Uint8Array(40);
    window.crypto.getRandomValues(arr);
    return Array.from(arr, dec2hex).join('');
  }
}


