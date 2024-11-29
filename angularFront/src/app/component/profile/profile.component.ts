import { Component, OnInit } from '@angular/core';
import { UserService } from '../../service/user.service';

@Component({
	selector: 'app-profile',
	standalone: true,
	imports: [],
	templateUrl: './profile.component.html',
	styleUrl: './profile.component.scss'
})
export class ProfileComponent {

	constructor(public userService: UserService) {}

	ngOnInit() {
		const userId = localStorage.getItem("userId");

    if (userId !== null) {
        this.userService.getUserById(Number(userId));
    } else {
        console.error('User ID not found in localStorage');
    }
	}
}
