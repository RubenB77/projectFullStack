import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { User } from '../../model/user';

@Component({
	selector: 'app-login',
	standalone: true,
	imports: [FormsModule, CommonModule, RouterLink],
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.scss'] 
})
export class LoginComponent {
	username: string = '';
	password: string = '';
	errorMessage: string = '';

	constructor(private authService: AuthService, private router: Router) { }

	login(event: Event) {
		event.preventDefault();
		this.authService.login(this.username, this.password).subscribe({
			next: response => {
				const redirectUrl = sessionStorage.getItem('redirectUrl') || '/home';
				sessionStorage.removeItem('redirectUrl');
				this.router.navigate([redirectUrl]);
			},
			error: err => {
				this.errorMessage = 'Login failed. Please check your credentials and try again.';
			}
		});
	}
}
