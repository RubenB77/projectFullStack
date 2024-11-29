// src/main.ts
import { importProvidersFrom  } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter, Routes } from '@angular/router';
import { AppComponent } from './app/app.component';
import { provideHttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { routes } from './app/app.routes'
import { UserService } from './app/service/user.service';


bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes), // Set up routing with provideRouter
    provideHttpClient(), // Use provideHttpClient instead of HttpClientModule
    importProvidersFrom(FormsModule), // Import necessary modules
    UserService // Register any required services
  ]
}).catch(err => console.error(err));