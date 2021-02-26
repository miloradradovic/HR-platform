import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatSelectChange} from '@angular/material/select';
import {Router} from '@angular/router';
import {CandidateService} from '../../services/candidate-service/candidate.service';
import {SkillService} from '../../services/skill-service/skill.service';

@Component({
  selector: 'app-all-candidates-page',
  templateUrl: './all-candidates-page.component.html',
  styleUrls: ['./all-candidates-page.component.css']
})
export class AllCandidatesPageComponent implements OnInit {

  candidates = [];
  skills = [];
  form: FormGroup;
  selectedSkills = new FormControl();
  candidateNameInput = '';
  filterType = 'full name';

  constructor(private fb: FormBuilder,
              private snackBar: MatSnackBar,
              private router: Router,
              private candidateService: CandidateService,
              private skillService: SkillService) {
  }

  ngOnInit(): void {
    this.setupData();
  }

  private setupData(): void {
    this.candidateService.getAllCandidates().subscribe(
      result => {
        this.candidates = result;
      },
      error => {
        this.snackBar.open('Something went wrong!', 'Ok', {duration: 2000});
      }
    );
    this.skillService.getAllSkills().subscribe(
      result => {
        this.skills = result;
      },
      error => {
        this.snackBar.open('Something went wrong!', 'Ok', {duration: 2000});
      }
    );
  }

  onSelectChange($event: MatSelectChange): void{
    this.filterType = $event.value; // change the filter type
  }

  goToDetailed($event: number): void{
    this.router.navigate(['/detailed-candidate'], {queryParams: {candidate_id: $event}});
  }

  onDelete($event: number): void{
    this.candidateService.deleteCandidate($event).subscribe(
      result => {
        const newList = [];
        this.candidates.forEach((item, index) => {
          if (item.id !== $event){
            newList.push(item);
          }
        });
        this.candidates = newList;
        this.snackBar.open('Successfully deleted the candidate!', 'Ok', {duration: 2000});
      },
      error => {
      }
    );
  }

  search(): void{
    if (this.filterType === 'full name'){ // search by name
      const searchParams = {param: 'name', value: this.candidateNameInput};
      this.candidateService.search(searchParams).subscribe(
        result => {
          this.candidates = result;
        },
        error => {
          this.snackBar.open('Something went wrong!', 'Ok', {duration: 2000});
        }
      );
    }else { // search by skill(s)
      const selected = this.selectedSkills.value;
      if (selected !== null){ // selected === null if nothing is selected
        let searchValue = '';
        if (selected.size !== 0){ // if more than one skill was selected, make the searchValue like 'skill1,skill2', otherwise do nothing
          selected.forEach((item, index) => {
            searchValue += item;
            searchValue += ',';
          });
          searchValue = searchValue.slice(0, -1); // removes the last ','
          const searchParams = {param: this.filterType, value: searchValue};
          this.candidateService.search(searchParams).subscribe(
            result => {
              this.candidates = result;
            },
            error => {
              this.snackBar.open('Something went wrong!', 'Ok', {duration: 2000});
            }
          );
        }
      }else{ // nothing is selected, so get all the candidates
        this.candidateService.getAllCandidates().subscribe(
          result => {
            this.candidates = result;
          },
          error => {
            this.snackBar.open('Something went wrong!', 'Ok', {duration: 2000});
          }
        );
      }
    }
  }
}
