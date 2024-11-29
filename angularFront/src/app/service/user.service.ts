import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../model/user';
import { Observable } from 'rxjs';

@Injectable()
export class UserService {

    private baseUrl = 'http://localhost:8080';

    constructor(private http: HttpClient) { }

    public getUsers(): Observable<User[]> {
        return this.http.get<User[]>(`${this.baseUrl}/users`);
    }

    public getUserById(id: number): Observable<User> {
        return this.http.get<User>(`${this.baseUrl}/user/${id}`);
    }

    public addUser(user: User): Observable<User> {
        return this.http.post<User>(`${this.baseUrl}/user`, user, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json'
            })
        });
    }

    public register(user: User): Observable<User> {
        return this.http.post<User>(`${this.baseUrl}/register`, user, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json'
            })
        });
    }


    public deleteUserById(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/user/${id}`);
    }

    public login(username: string, password: string): Observable<any> {
        const body = { username, password };
        return this.http.post<any>(`${this.baseUrl}/login`, body, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json'
            }),
            withCredentials: true
        });
    }    

}