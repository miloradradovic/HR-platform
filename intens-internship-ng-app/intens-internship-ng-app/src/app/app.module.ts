import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { TableComponent } from './components/table/table.component';
import { AllCandidatesPageComponent } from './components/all-candidates-page/all-candidates-page.component';
import { DetailedCandidatePageComponent } from './components/detailed-candidate-page/detailed-candidate-page.component';
import { AddCandidatePageComponent } from './components/add-candidate-page/add-candidate-page.component';
import { AddSkillPageComponent } from './components/add-skill-page/add-skill-page.component';
import { AllSkillsPageComponent } from './components/all-skills-page/all-skills-page.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  declarations: [
    AppComponent,
    TableComponent,
    AllCandidatesPageComponent,
    DetailedCandidatePageComponent,
    AddCandidatePageComponent,
    AddSkillPageComponent,
    AllSkillsPageComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
