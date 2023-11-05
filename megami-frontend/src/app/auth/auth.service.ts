import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, catchError, of, switchMap, tap } from 'rxjs';
import { Response } from 'src/models/Response';

import { environment } from 'environments/environments';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  apiUrl = environment?.apiUrl || 'http://localhost:8080';

  constructor(
    private http: HttpClient
  ) { }

  loggedInUser = new BehaviorSubject<String | null>(null);

  isLoggedIn(): Observable<String | null> {
    return this.http.get<Response>(
      `${this.apiUrl}/auth/login`
    ).pipe(
      switchMap(res => of(res.data.username)),
      catchError(err => of(null)),
      tap(loggedInUser => this.loggedInUser.next(loggedInUser))
    );
  }

  registerUser(username: string, password: string): Observable<Response> {
    return this.http.post<Response>(
      `${this.apiUrl}/auth/register`,
      { username, password }
    );
  }

  loginUser(username: string, password: string): Observable<Response> {
    return this.http.post<Response>(
      `${this.apiUrl}/auth/login`,
      { username, password }
    ).pipe(
      tap(res => {
        localStorage.setItem("token", res.data.token);
        this.loggedInUser.next(username);
      })
    );
  }

  deleteAccount() {
    return this.http.delete<Response>(
      `${this.apiUrl}/users/${this.loggedInUser.getValue()}`
    ).pipe(tap(() => this.logout()));
  }

  logout() {
    localStorage.removeItem("token");
    this.loggedInUser.next(null);
  }
}
