import { Component, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { ExpenseService } from '../../service/expense.service';
import { Expense } from '../../model/expense';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { Category } from '../../model/category';
import { CategoryService } from '../../service/category.service';
declare var bootstrap: any;

@Component({
	selector: 'app-home',
	standalone: true,
	imports: [ReactiveFormsModule, CommonModule, RouterLink],
	templateUrl: './expense.component.html',
	styleUrls: ['./expense.component.scss']
})
export class ExpenseComponent {
	@ViewChild('expenseMapContainer', { static: false }) mapContainer!: ElementRef;

	expenses: Expense[] = [];
	selectedExpense: Expense | null = null;
	expenseDetailForm: FormGroup;
	map!: google.maps.Map;
	AdvancedMarkerElement!: typeof google.maps.marker.AdvancedMarkerElement;
	PinElement!: typeof google.maps.marker.PinElement;
	categories: Category[] = [];

	constructor(private fb: FormBuilder, public authService: AuthService, private router: Router, public expenseService: ExpenseService, public categoryService: CategoryService) {
		this.expenseDetailForm = this.fb.group({
			expenseName: [''],
			expensePrice: [null],
			expenseCategory: [null],
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
			this.categoryService.getCategoriesByUserId(userId).subscribe({
                next: categories => {
                    this.categories = categories;
                },
                error: (err) => {
                    console.error('Error fetching categories', err);
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

	async ngAfterViewInit(): Promise<void> {
		if (this.selectedExpense?.expenseAddress) {
			await this.initializeMap();
		}
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

	openModal(expense: Expense): void {
		this.selectedExpense = expense;
		this.expenseDetailForm.get('expenseCategory')?.setValue(expense?.expenseCategory || "");
	
		this.expenseDetailForm.patchValue({
			expenseName: expense.expenseName,
			expenseCategory: expense.expenseCategory,
			expensePrice: expense.expensePrice,
			expenseDate: expense.expenseDate,
			expenseNote: expense.expenseNote
		});
	
		setTimeout(() => {
			if (this.selectedExpense?.expenseAddress) {
				this.initializeMap();
			}
		}, 300);
	}
	
	
	saveChanges(): void {
		if (this.expenseDetailForm.valid && this.selectedExpense) {
			const { expenseCategory, ...expenseDetails } = this.expenseDetailForm.value;
			const updatedExpense = {
				...this.selectedExpense,
				...expenseDetails,
				categoryId: this.expenseDetailForm.value.expenseCategory?.categoryId || this.selectedExpense.expenseCategory!.categoryId,
			};

			const userId = parseInt(localStorage.getItem('userId')!, 10);
	
			this.expenseService.updateExpense(userId, this.selectedExpense.expenseId!, updatedExpense).subscribe({
				next: (response) => {
					// Update the local expense list with the updated expense
					const index = this.expenses.findIndex(e => e.expenseId === this.selectedExpense?.expenseId);
					if (index !== -1) {
						this.expenses[index] = response;
					}
	
					// Close the modal
					const modalElement = document.getElementById('expenseDetailsModal');
					if (modalElement) {
						const modal = bootstrap.Modal.getInstance(modalElement);
						modal?.hide();
					}
				},
				error: (err) => {
					console.error('Error updating expense:', err);
				},
			});
		}
	}
	

	async initializeMap(): Promise<void> {
		const { Map } = await google.maps.importLibrary('maps') as google.maps.MapsLibrary;
		const { AdvancedMarkerElement, PinElement } = await google.maps.importLibrary('marker') as any;

		this.AdvancedMarkerElement = AdvancedMarkerElement;
		this.PinElement = PinElement;

		const { latitude: lat, longitude: lng } = this.selectedExpense!.expenseAddress!;

		// Create the map centered at the selected expense address
		this.map = new Map(this.mapContainer.nativeElement, {
			center: { lat, lng },
			zoom: 15,
			mapId: 'DEMO_MAP_ID', // Replace with your custom Map ID if applicable
		});

		// Add a marker
		this.addMarker(lat, lng);
	}

	addMarker(lat: number, lng: number): void {
		const pinBackground = new this.PinElement({
			background: '#FF5733', // Customize pin color
		});

		const marker = new this.AdvancedMarkerElement({
			map: this.map,
			position: { lat, lng },
			content: pinBackground.element,
			gmpClickable: true,
		});
	}
}