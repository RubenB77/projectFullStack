import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthService } from './service/auth.service';

@Injectable({
    providedIn: 'root',
})
export class AuthGuard implements CanActivate {
    constructor(private authService: AuthService, private router: Router) {}

    canActivate(): Observable<boolean | UrlTree> {
        return this.authService.isAuthenticated$.pipe(
            map((isAuthenticated: boolean) => { // Explicitly type 'isAuthenticated' as boolean
                if (isAuthenticated) {
                    return true; 
                } else {
                    sessionStorage.setItem('redirectUrl', this.router.url);
                    return this.router.createUrlTree(['/login']);
                }
            })
        );
    }
}
