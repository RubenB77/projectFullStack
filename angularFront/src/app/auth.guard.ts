import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { AuthService } from './service/auth.service';

@Injectable({
    providedIn: 'root',
})
export class AuthGuard implements CanActivate {
    constructor(private authService: AuthService, private router: Router) {}

    canActivate(): Observable<boolean | UrlTree> {
        return this.authService.checkAuthStatus().pipe(
            switchMap((isAuthenticated) => {
                return this.authService.isAuthenticated$.pipe(
                    map((authStatus) => {
                        if (authStatus) {
                            return true;
                        } else {
                            sessionStorage.setItem('redirectUrl', this.router.url);
                            return this.router.createUrlTree(['/login']);
                        }
                    })
                );
            })
        );
    }
}
