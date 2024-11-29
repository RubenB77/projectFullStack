import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { ExpenseService } from '../../service/expense.service';
import { Expense } from '../../model/expense';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';

@Component({
	selector: 'app-home',
	standalone: true,
	imports: [ReactiveFormsModule, CommonModule, RouterLink],
	templateUrl: './expense.component.html',
	styleUrls: ['./expense.component.scss']
})
export class ExpenseComponent {

	expenses: Expense[] = [];
	selectedExpense: Expense | null = null; 
    expenseDetailForm: FormGroup;

	constructor(private fb: FormBuilder, public authService: AuthService, private router: Router, public expenseService: ExpenseService) { 
		this.expenseDetailForm = this.fb.group({
            expenseName: [''],
            expensePrice: [null],
            expenseDate: [''],
            expenseNote: ['']
        });
	}

	ngOnInit(): void {

		const userId = parseInt(localStorage.getItem('userId')!, 10);

		if (userId) {
			this.expenseService.getExpensesByUserId(userId).subscribe({
				next: (expenses) => {
					this.expenses = expenses;
					this.sort("createdAt");
				},
				error: (err) => {
					console.error('Error fetching expenses', err);
				}
			});
		} else {
			console.error('User ID not found in localStorage');
		}
	}

	currentSortColumn: string = "createdAt";
	isAscending: boolean = true;

	sort(column: keyof Expense): void {
		if (this.currentSortColumn === column) {
			this.isAscending = !this.isAscending;
		} else {
			this.currentSortColumn = column;
			this.isAscending = true;
		}

		this.expenses.sort((a, b) => {
			const valueA = a[column];
			const valueB = b[column];

			if (typeof valueA === 'string') {
				return this.isAscending
					? valueA.localeCompare(valueB as string)
					: (valueB as string).localeCompare(valueA);
			} else if (valueA instanceof Date) {
				return this.isAscending
					? +new Date(valueA) - +new Date(valueB as Date)
					: +new Date(valueB as Date) - +new Date(valueA);
			} else {
				return this.isAscending ? (valueA as number) - (valueB as number) : (valueB as number) - (valueA as number);
			}
		});
	}


	getTextColor(backgroundColor: string): string {
		if (!backgroundColor) {
			return '#FFFFFF';
		}

		const hex = backgroundColor.replace('#', '');
		const r = parseInt(hex.substring(0, 2), 16);
		const g = parseInt(hex.substring(2, 4), 16);
		const b = parseInt(hex.substring(4, 6), 16);

		const luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b;

		return luminance < 128 ? '#FFFFFF' : '#000000';
	}


	isLastRow(index: number): boolean {
		return index === this.expenses.length - 1;
	}

	isFirstCell(cell: any): boolean {
		return cell === this.expenses[0].createdAt;
	}

	isLastCell(cell: any): boolean {
		return cell === this.expenses[0].expenseNote;
	}


	logObservable() {
		this.authService.isAuthenticated$.subscribe(
			value => console.log(value)
		)
	}
}
