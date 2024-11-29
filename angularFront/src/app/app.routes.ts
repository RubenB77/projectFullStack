import { Routes } from '@angular/router';
import { HomeComponent } from './component/home/home.component';
import { UserFormComponent } from './component/user-form/user-form.component';
import { UserListComponent } from './component/user-list/user-list.component';
import { LoginComponent } from './component/login/login.component';
import { SignupComponent } from './component/signup/signup.component';
import { ExpenseComponent } from './component/expense/expense.component';
import { AddExpenseComponent } from './component/add-expense/add-expense.component';
import { AuthGuard } from './auth.guard';
import { MapComponent } from './component/map/map.component';


// Define application routes directly in main.ts
export const routes: Routes = [
    { path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: 'home', component: HomeComponent },
    { path: 'users', component: UserListComponent },
    { path: 'adduser', component: UserFormComponent },
    { path: 'login', component: LoginComponent },
    { path: 'signup', component: SignupComponent },
    { path: 'user/expenses', component: ExpenseComponent, canActivate:[AuthGuard]},
    { path:'user/expenses/add', component: AddExpenseComponent, canActivate:[AuthGuard]},
    { path:'user/map', component: MapComponent, canActivate:[AuthGuard]},
    { path: '**', redirectTo: 'login'}
  ];
  
