import {Routes} from '@angular/router';
import {AllSkillsPageComponent} from '../components/all-skills-page/all-skills-page.component';
import {AllCandidatesPageComponent} from '../components/all-candidates-page/all-candidates-page.component';
import {AddCandidatePageComponent} from '../components/add-candidate-page/add-candidate-page.component';
import {AddSkillPageComponent} from '../components/add-skill-page/add-skill-page.component';
import {DetailedCandidatePageComponent} from '../components/detailed-candidate-page/detailed-candidate-page.component';

export const routes: Routes = [
  {
    path: '',
    component: AllCandidatesPageComponent
  },
  {
    path: 'skills',
    component: AllSkillsPageComponent
  },
  {
    path: 'add-candidate',
    component: AddCandidatePageComponent
  },
  {
    path: 'add-skill',
    component: AddSkillPageComponent
  },
  {
    path: 'detailed-candidate',
    component: DetailedCandidatePageComponent
  }
];
