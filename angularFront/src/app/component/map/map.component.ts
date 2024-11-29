import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { Loader } from '@googlemaps/js-api-loader';
import { AuthService } from '../../service/auth.service';
import { ExpenseService } from '../../service/expense.service';

@Component({
	selector: 'app-map',
	template: `<div #mapContainer style="width: 100%; height: 500px;"></div>`,
	styleUrls: ['./map.component.scss'],
})
export class MapComponent implements OnInit {

	constructor(public expenseService: ExpenseService, public authService: AuthService) {}

	@ViewChild('mapContainer', { static: true }) mapContainer!: ElementRef;
	map!: google.maps.Map;

	// Array of coordinates for the markers
	locations: { lat: number; lng: number }[] = [];

	ngOnInit() {

		this.authService.checkAuthStatus();

		const userId = parseInt(localStorage.getItem('userId')!, 10);

		const loader = new Loader({
			apiKey: 'AIzaSyAAu7G1xFIEjPPZ1i9ksc3v4X9-8Y_2NyI',
			version: 'weekly',
		});

		loader.load().then(() => {
			this.map = new google.maps.Map(this.mapContainer.nativeElement, {
				center: { lat: 48.857654770923126, lng: 2.345869920318469 },
				zoom: 12,
			});
		
			// Ensure map instance is ready before adding markers
			if (this.map) {
				this.expenseService.getExpensesByUserId(userId).subscribe({
					next: (expenses) => {
						this.locations = expenses
							.filter(expense => expense.expenseAddress !== null)
							.map(expense => ({
								lat: expense.expenseAddress!.latitude,
								lng: expense.expenseAddress!.longitude,
							}));
						// Add markers only if locations array has items
						if (this.locations.length > 0) {
							this.locations.forEach((location) => {
								new google.maps.Marker({
									position: location,
									map: this.map,
								});
							});
						} else {
							console.warn('No valid locations found.');
						}
					},
					error: (err) => console.error('Error fetching expenses:', err),
				});
			} else {
				console.error('Map instance is not initialized.');
			}
		});
	}
}
