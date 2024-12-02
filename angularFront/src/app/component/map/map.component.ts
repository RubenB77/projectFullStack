import { Component, ElementRef, ViewChild, OnInit } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { ExpenseService } from '../../service/expense.service';
import { Expense } from '../../model/expense';

@Component({
	selector: 'app-map',
	template: `<div #mapContainer style="width: 100%; height: 500px;"></div>`,
	styleUrls: ['./map.component.scss'],
})
export class MapComponent implements OnInit {
	@ViewChild('mapContainer', { static: true }) mapContainer!: ElementRef;
	map!: google.maps.Map;
	AdvancedMarkerElement!: typeof google.maps.marker.AdvancedMarkerElement;
	PinElement!: typeof google.maps.marker.PinElement;

	constructor(public expenseService: ExpenseService, public authService: AuthService) { }

	async ngOnInit(): Promise<void> {
		this.authService.checkAuthStatus();
		const userId = parseInt(localStorage.getItem('userId')!, 10);

		// Initialize the map and required marker libraries
		await this.initMap();

		// Fetch expenses and add markers
		this.expenseService.getExpensesByUserId(userId).subscribe({
			next: (expenses) => {
				if (expenses && expenses.length > 0) {
					this.addMarkers(expenses);
				} else {
					console.warn('No valid locations found.');
				}
			},
			error: (err) => console.error('Error fetching expenses:', err),
		});
	}

	private async initMap(): Promise<void> {
		const { Map } = await google.maps.importLibrary("maps") as google.maps.MapsLibrary;
		const { AdvancedMarkerElement, PinElement } = await google.maps.importLibrary("marker") as any;

		// Save references to marker classes for later use
		this.AdvancedMarkerElement = AdvancedMarkerElement;
		this.PinElement = PinElement;

		this.map = new Map(this.mapContainer.nativeElement, {
			center: { lat: 48.857654770923126, lng: 2.345869920318469 },
			zoom: 12,
			mapId: 'DEMO_MAP_ID',
		});
	}

	private addMarkers(expenses: Expense[]): void {
		const infoWindow = new google.maps.InfoWindow({disableAutoPan: true});
		expenses
			.filter((expense) => expense.expenseAddress !== null)
			.forEach((expense) => {
				const { latitude: lat, longitude: lng } = expense.expenseAddress!;
				const color = expense.expenseCategory?.categoryColor || '#FF0000';

				const pinBackground = new this.PinElement({
					background: color,
				});

				const marker = new this.AdvancedMarkerElement({
					map: this.map,
					position: { lat, lng },
					content: pinBackground.element,
					gmpClickable: true,
				});

				marker.addListener('click', () => {
					infoWindow.setContent(`
						<div style="font-size: 14px; color: black; background-color: white; padding: 10px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.3); cursor: pointer;">
						<h4>${expense.expenseName}</h4>
						<p><strong>Amount:</strong> €${expense.expensePrice?.toFixed(2) || '0.00'}</p>
						<p><strong>Date:</strong> ${expense.expenseDate || 'Unknown Date'}</p>
						<p><strong>Description:</strong> ${expense.expenseAddress}</p>
					</div>
					`);
					infoWindow.setPosition({ lat, lng });
					infoWindow.open(marker.map);

					google.maps.event.addListener(infoWindow, 'domready', () => {
						const contentElement = document.querySelector('.gm-style-iw');
						if (contentElement) {
							contentElement.addEventListener('click', () => infoWindow.close());
						}
					});
				})
			});
	}
}
