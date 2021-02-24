import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Observable} from 'rxjs';
import {MatSnackBar} from '@angular/material/snack-bar';
import {SkillService} from '../../services/skill-service/skill.service';
import {CandidateService} from '../../services/candidate-service/candidate.service';
import {ActivatedRoute, Router} from '@angular/router';
import {map, startWith} from 'rxjs/operators';

@Component({
  selector: 'app-detailed-candidate-page',
  templateUrl: './detailed-candidate-page.component.html',
  styleUrls: ['./detailed-candidate-page.component.css']
})
export class DetailedCandidatePageComponent implements OnInit {

  form: FormGroup;
  myControl = new FormControl();
  options = []; // options for autocomplete
  filteredOptions: Observable<string[]>;
  skills = []; // to be shown on the table
  skillsBackend = []; // all skills
  years18: Date;
  candidate = null;
  idNew = 0;
  idPassed = 0;


  constructor(public snackBar: MatSnackBar,
              public fb: FormBuilder,
              private skillService: SkillService,
              private candidateService: CandidateService,
              private router: Router,
              private activatedRoute: ActivatedRoute) {
    this.form = this.fb.group({
      fullNameInput: ['', Validators.required],
      emailInput: ['', Validators.required],
      contactNumberInput: ['', Validators.required],
      dateInput: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.idPassed = Number(this.activatedRoute.snapshot.queryParamMap.get('candidate_id'));
    this.setupData();
  }

  private setupData(): void{
    this.skillService.getAllSkills().subscribe(
      result => {
        this.skillsBackend = result; // all skills

        this.candidateService.getCandidateById(this.idPassed).subscribe(
          result2 => {
            this.candidate = result2; // candidate found by id
            this.form.controls.fullNameInput.setValue(this.candidate.fullName);
            this.form.controls.emailInput.setValue(this.candidate.email);
            this.form.controls.contactNumberInput.setValue(this.candidate.contactNumber);
            this.form.controls.dateInput.setValue(this.candidate.dateOfBirth);
            const skillList = [];
            this.candidate.skills.forEach((item, index) => {
              this.skillsBackend.forEach((item2, index2) => {
                if (item2.name === item){
                  const skillObj = {id: item2.id, name: item2.name};
                  skillList.push(skillObj);
                }
              });
            });
            this.skills = skillList;
            console.log(this.skills);
            console.log(this.skillsBackend);
            console.log(this.candidate);
            this.skillsBackend.forEach((item, index) => {
              let check = false;
              this.skills.forEach((item2, index2) => {
                if (item2.id === item.id){
                  check = true;
                }
              });
              if (check === false){
                this.options.push(item.name);
              }
            });
            this.filteredOptions = this.myControl.valueChanges
              .pipe(
                startWith(''),
                map(value => this._filter(value))
              );
          },
          error => {
            this.snackBar.open('Something went wrong!', 'Ok', {duration: 2000});
          }
        );
      },
      error => {
        this.snackBar.open('Something went wrong!', 'Ok', {duration: 2000});
      }
    );
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }

  addSkill(): void {
    if (this.myControl.value !== null){ // if the sent input is not empty
      const newList = [];
      let check = false;
      this.skills.forEach((item, index) => {
        newList.push(item);
        if (item.name === this.myControl.value){
          check = true; // check if the sent input is already in the list
        }
      });
      if (check === false){
        let found = false;
        this.skillsBackend.forEach((item, index) => {
          if (item.name === this.myControl.value){
            newList.push({id: item.id, name: item.name});
            found = true;
          }
        });
        if (found === false){
          this.idNew = this.idNew - 1;
          newList.push({id: this.idNew, name: this.myControl.value});
        }
        this.skills = newList;
        const skillListNames = [];
        this.skills.forEach((item, index) => {
          skillListNames.push(item.name);
        });
        this.candidate.skills = skillListNames;
        this.options = [];
        this.candidateService.updateCandidate(this.candidate).subscribe(
          result => {
            this.setupData();
          },
          error => {
            this.snackBar.open('Something went wrong!', 'Ok', {duration: 2000});
          }
        );
      }
    }
  }

  onDelete($event: number): void{
    const newList = [];
    let skill;

    this.skillsBackend.forEach((item, index) => {
      if (item.id === $event){
        skill = item;
      }
    });
    this.skills.forEach((item, index) => {
      if (item.name !== skill.name){
        newList.push(item);
      }
    });
    this.skills = newList;
    this.options = [];
    this.candidateService.removeSkillFromCandidate(this.idPassed, skill.id).subscribe(
      result => {
        this.setupData();
      },
      error => {
        this.snackBar.open('Something went wrong!', 'Ok', {duration: 2000});
      }
    );
  }
}

