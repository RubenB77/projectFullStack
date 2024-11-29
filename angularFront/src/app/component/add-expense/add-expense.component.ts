import { CommonModule, DatePipe } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CategoryService } from '../../service/category.service';
import { Category } from '../../model/category';
import { AuthService } from '../../service/auth.service';
import { ExpenseFormData } from '../../model/expense';
import { ExpenseService } from '../../service/expense.service';
import { AddressService } from '../../service/address.service';
import { debounceTime, switchMap, of } from 'rxjs';
import { Address } from '../../model/address.model';
import { Router } from '@angular/router';

@Component({
    selector: 'app-add-expense',
    templateUrl: './add-expense.component.html',
    styleUrls: ['./add-expense.component.scss'],
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    providers: [DatePipe]
})
export class AddExpenseComponent {
    expenseForm: FormGroup;
    addressForm: FormGroup;
    categoryForm: FormGroup;
    showCategoryMenu = false;
    categories: Category[] = [];
    colorPresets = ['#FF5733', '#33FF57', '#3357FF', '#F3FF33', '#FF33F3', '#33FFF3'];
    addressSuggestions: Address[] = [];
    showAddressMenu = false;


    constructor(private fb: FormBuilder, public categoryService: CategoryService, public expenseService: ExpenseService, public authService: AuthService, 
        public addressService: AddressService, private router: Router,) {
        // Expense Form Initialization
        this.expenseForm = this.fb.group({
            expenseName: ['', [Validators.required, Validators.minLength(3)]],
            expensePrice: [null, [Validators.required, Validators.min(0.01)]],
            expenseDate: [null, Validators.required],
            expenseNote: [''],
            expenseCategory: [null, Validators.required],
        });

        this.addressForm = this.fb.group({
            addressName: ['', [Validators.required]],
            postCode: [''],
            city: [''],
            latitude: ['', Validators.required],
            longitude: ['', Validators.required]
        });

        // Category Form Initialization
        this.categoryForm = this.fb.group({
            categoryName: ['', [Validators.required, Validators.minLength(3)]],
            categoryDescription: [''],
            categoryColor: ['#e0e0e0', Validators.required]
        });
    }

    ngOnInit(): void {
        this.authService.checkAuthStatus();

        const userId = parseInt(localStorage.getItem('userId')!, 10);

        if (userId) {
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

    toggleCategoryMenu(): void {
        this.showCategoryMenu = !this.showCategoryMenu;
    }

    setCategoryColor(color: string): void {
        this.categoryForm.get('categoryColor')?.setValue(color);
    }

    createCategory(): void {
        if (this.categoryForm.valid) {
            const userId = Number(localStorage.getItem('userId'));
            const newCategory = this.categoryForm.value;
            this.categoryService.postCategoryUser(newCategory, userId).subscribe({
                next: category => {
                    this.categoryService.getCategoriesByUserId(userId).subscribe({
                        next: categories => {
                            this.categories = categories
                        }
                    });
                    this.categories.push(newCategory);
                    this.categoryForm.reset({ color: '#e0e0e0' });
                    this.toggleCategoryMenu();
                }
            });
        }
    }

    onAddressInputChange(event: Event): void {
        const inputElement = event.target as HTMLInputElement;
        const query = inputElement.value;
        if (query.length > 3) {
            this.addressService.getAddressSuggestions(query).subscribe(suggestions => {
                this.addressSuggestions = suggestions;
                this.showAddressMenu = this.addressSuggestions.length > 0;
            });
        }
        else {
            // If less than 3 characters, clear the suggestions and hide the menu
            this.addressSuggestions = [];
            this.showAddressMenu = false;
        }
    }

    onAddressSelect(suggestion: Address): void {
        this.addressForm.setValue({
            addressName: suggestion.addressName,
            postCode: suggestion.postCode || '', 
            city: suggestion.city || '',         
            latitude: suggestion.latitude || 0, 
            longitude: suggestion.longitude || 0 
        });
        this.showAddressMenu = false;
    }

    onSubmit(): void {
        if (this.expenseForm.valid) {
            const userId = Number(localStorage.getItem('userId'));
            const expenseData: ExpenseFormData = {
                ...this.expenseForm.value,
                categoryId: this.expenseForm.value.expenseCategory.categoryId,
            };

            if (this.addressForm.value) {
                expenseData.expenseAddress = this.addressForm.value ;
            }    
            this.expenseService.postExpenseUser(userId, expenseData).subscribe({
                next: () => { 
                    this.router.navigate(['/user/expenses']);
                },
                error: err => console.error('Error saving expense:', err)
            });
        }
    }
}
