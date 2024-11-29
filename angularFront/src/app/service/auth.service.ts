import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private isAuthenticated = new BehaviorSubject<boolean>(false); 
    public isAuthenticated$: Observable<boolean> = this.isAuthenticated.asObservable();
    private baseUrl = 'http://localhost:8080';

    constructor(private http: HttpClient) {
        this.checkAuthStatus(); 
    }

    public checkAuthStatus(): void {
        this.http
            .get<{ success?: string; userId?: string; error?: string }>(
                `${this.baseUrl}/auth/check`,
                { withCredentials: true } 
            )
            .subscribe({
                next: (response) => {
                    if (response.success && response.userId) {
                        this.isAuthenticated.next(true); 
                        localStorage.setItem('userId', response.userId);
                    } else {
                        this.isAuthenticated.next(false); 
                        localStorage.removeItem('userId'); 
                    }
                },
                error: () => {
                    this.isAuthenticated.next(false); 
                    localStorage.removeItem('userId'); 
                },
            });
    }

    public login(username: string, password: string): Observable<boolean> {
        const body = { username, password };
        return this.http
            .post<{ accessToken?: string }>(`${this.baseUrl}/login`, body, {
                headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
                withCredentials: true,
            })
            .pipe(
                map((response) => {
                    if (response && response.accessToken) {
                        this.isAuthenticated.next(true); 
						this.checkAuthStatus();
                        return true;
                    }
                    this.isAuthenticated.next(false); 
                    return false;
                })
            );
    }

    public logout(): void {
        this.http.post(`${this.baseUrl}/logout`, {}, { withCredentials: true }).subscribe({
            next: () => {
                this.isAuthenticated.next(false); 
                localStorage.removeItem('userId'); 
            },
            error: (err) => {
                console.error('Logout failed:', err);
            },
        });
    }
}
