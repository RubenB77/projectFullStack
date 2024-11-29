import { Address } from "./address.model";
import { Category } from "./category";
import { User } from "./user";

export interface Expense {
    expenseId?: number;
    expenseName?: string;
    expensePrice?: number;
    expenseNote?: string;
    expenseDate?: Date;
    expenseAddress?: Address;
    expenseCategory?: Category;
    expenseUser?: User;
    createdAt?: Date;
}

export interface ExpenseFormData {
    expenseName: string;
    expensePrice: number;
    expenseNote?: string;
    expenseDate: Date;
    expenseAddress?: Address;
    categoryId: number;
}