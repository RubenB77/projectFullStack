import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Expense, ExpenseFormData } from '../model/expense';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ExpenseService {

	private baseUrl = 'http://localhost:8080';

	constructor(private http: HttpClient) { }

	getExpensesByUserId(userId: number): Observable<Expense[]> {
        return this.http.get<Expense[]>(`${this.baseUrl}/user/${userId}/expenses`, {withCredentials: true});
    }

    postExpenseUser(userId: number, expense: ExpenseFormData): Observable<Expense> {
        return this.http.post<Expense>(
            `${this.baseUrl}/user/${userId}/expense`, 
            expense,
            {withCredentials: true});
    }
}
