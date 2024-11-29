import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Address } from '../model/address.model'; 

@Injectable({
	providedIn: 'root'
})
export class AddressService {
	private apiUrl = 'https://api-adresse.data.gouv.fr/search';

	constructor(private http: HttpClient) { }

	getAddressSuggestions(query: string): Observable<Address[]> {
		if (!query || query.length < 3) {
			return of([]); // Return an empty array if the query is too short
		}

		const url = `${this.apiUrl}?q=${encodeURIComponent(query)}&limit=5`; // Add a limit to the number of results

		return this.http.get<{ features: any[] }>(url).pipe(
			
			map(response => {
				// Ensure the structure is correct and extract address information
				return response.features ? response.features.map(feature => {
					const properties = feature.properties;
					const geometry = feature.geometry;
					// Create and return Address model
					return new Address(
						properties.label,        // addressName
						properties.postcode,     // postCode
						properties.city,          // city
						geometry.coordinates[1],
						geometry.coordinates[0]
					);
				}) : [];
			}),
			catchError(err => {
				console.error('Error fetching address suggestions:', err);
				return of([]); // Return an empty array in case of error
			})
		);
	}
}
