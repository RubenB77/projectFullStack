import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from './service/auth.service';


@Component({
	selector: 'app-root',
	standalone: true,
	imports: [RouterOutlet, RouterLink],
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent {


	constructor(public authService: AuthService) {}
}
