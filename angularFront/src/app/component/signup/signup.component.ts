import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { UserService } from '../../service/user.service';
import { User } from '../../model/user';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
    selector: 'app-signup',
    standalone: true,
    imports: [ReactiveFormsModule, CommonModule], // Add ReactiveFormsModule here
    templateUrl: './signup.component.html',
    styleUrls: ['./signup.component.scss']
})
export class SignupComponent {

	constructor(
		private router: Router,
		private userService: UserService,
        private authService: AuthService) {}

    userForm = new FormGroup({
        username: new FormControl('', [
            Validators.required,
            Validators.minLength(4)
        ]),
        password: new FormControl('', [
            Validators.required,
            Validators.minLength(8),
            Validators.maxLength(16),
            Validators.pattern("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]*$")
        ]),
		confirmPassword: new FormControl('', [
            Validators.required,
        ]),
        email: new FormControl('', [
            Validators.required,
            Validators.email
        ])
    }, { validators: this?.passwordMatchValidator });

    user: User = new User({});

    ngOnInit() {
        this.userForm.valueChanges.subscribe((value) => {
            Object.assign(this.user, {
                username: value.username || undefined, 
                password: value.password || undefined, 
                email: value.email || undefined
            });
        });
    }

    passwordMatchValidator(group: AbstractControl): ValidationErrors | null {
        const password = group.get('password')?.value;
        const confirmPassword = group.get('confirmPassword')?.value;
        return password === confirmPassword ? null : { passwordMismatch: true };
    }

    onSubmit() {
		if (this.userForm.valid) {
			const hasNullValues = Object.values(this.user).some(value => value === null);
			
			if (hasNullValues) {
				console.error('Form submission failed: Some user fields are null.');
				return; 
			}
	
			this.userService.register(this.user).subscribe(result => this.authService.login(this.user.getUserId, this.user.getUserPassword));
            this.router.navigate(['/login']);

		} else {
			console.error('Form is invalid:', this.userForm.errors);
		}
	}
}
