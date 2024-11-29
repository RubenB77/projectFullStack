import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../service/user.service';
import { User } from '../../model/user';

@Component({
	selector: 'app-user-form',
	standalone: true,
	imports: [FormsModule, CommonModule],
	templateUrl: './user-form.component.html',
	styleUrls: ['./user-form.component.css']
})
export class UserFormComponent {

	user: User;

	constructor(
		private route: ActivatedRoute,
		private router: Router,
		private userService: UserService) {
		this.user = new User({});
	}

	postUser() {
		this.userService.addUser(this.user).subscribe(result => this.gotoUserList());
	}

	gotoUserList() {
		this.router.navigate(['/users']);
	}
}