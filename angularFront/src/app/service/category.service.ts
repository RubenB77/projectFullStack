import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Category } from '../model/category';

@Injectable({
	providedIn: 'root'
})
export class CategoryService {

	private baseUrl = 'http://localhost:8080';

	constructor(private http: HttpClient) { }

	getCategoriesByUserId(userId: number): Observable<Category[]> {
		return this.http.get<Category[]>(`${this.baseUrl}/user/${userId}/categories`, { withCredentials: true });
	}

	postCategoryUser(category: Category, userId: number): Observable<Category> {
		return this.http.post<Category>(
			`${this.baseUrl}/user/${userId}/category`,
			category,
			{ withCredentials: true });
	}

	getCategoryUser(userId: number): Observable<Category> {
		return this.http.get<Category>(`${this.baseUrl}/user/${userId}/category`, { withCredentials: true });
	}
}
