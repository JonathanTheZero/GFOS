import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Todo } from './interfaces/dashboard.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { todoSamples } from './mock.data';

@Injectable({
  providedIn: 'root'
})

export class ApiService {

  private readonly url: string = "";

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

  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }
}


