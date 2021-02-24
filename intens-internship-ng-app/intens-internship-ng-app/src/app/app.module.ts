import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { TableComponent } from './components/shared/table/table.component';
import { AllCandidatesPageComponent } from './components/all-candidates-page/all-candidates-page.component';
import { DetailedCandidatePageComponent } from './components/detailed-candidate-page/detailed-candidate-page.component';
import { AddCandidatePageComponent } from './components/add-candidate-page/add-candidate-page.component';
import { AddSkillPageComponent } from './components/add-skill-page/add-skill-page.component';
import { AllSkillsPageComponent } from './components/all-skills-page/all-skills-page.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatTableModule} from '@angular/material/table';
import {MatButtonModule} from '@angular/material/button';
import { NavigationComponent } from './components/shared/navigation/navigation.component';
import {RouterModule} from '@angular/router';
import {routes} from './routing/routes';
import {MatMenuModule} from '@angular/material/menu';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatInput, MatInputModule} from '@angular/material/input';
import {HttpClientModule} from '@angular/common/http';
import {MatDividerModule} from '@angular/material/divider';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatNativeDateModule} from '@angular/material/core';

@NgModule({
  declarations: [
    AppComponent,
    TableComponent,
    AllCandidatesPageComponent,
    DetailedCandidatePageComponent,
    AddCandidatePageComponent,
    AddSkillPageComponent,
    AllSkillsPageComponent,
    NavigationComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatTableModule,
    MatButtonModule,
    RouterModule.forRoot(routes),
    MatMenuModule,
    MatToolbarModule,
    MatIconModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatSelectModule,
    ReactiveFormsModule,
    FormsModule,
    MatSnackBarModule,
    MatInputModule,
    MatPaginatorModule,
    HttpClientModule,
    MatDatepickerModule,
    MatAutocompleteModule,
    MatNativeDateModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
