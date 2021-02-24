import { Component, OnInit } from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import {SkillService} from '../../services/skill-service/skill.service';
import {CandidateService} from '../../services/candidate-service/candidate.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-add-candidate-page',
  templateUrl: './add-candidate-page.component.html',
  styleUrls: ['./add-candidate-page.component.css']
})
export class AddCandidatePageComponent implements OnInit {

  form: FormGroup;
  myControl = new FormControl();
  options = [];
  filteredOptions: Observable<string[]>;
  skills = [];
  skillsBackend = [];
  years18: Date;
  idNew = 0;

  constructor(public snackBar: MatSnackBar,
              public fb: FormBuilder,
              private skillService: SkillService,
              private candidateService: CandidateService,
              private router: Router) {
    this.form = this.fb.group({
        fullNameInput: ['', Validators.required],
        emailInput: ['', Validators.required],
        contactNumberInput: ['', Validators.required],
        dateInput: ['', Validators.required],
      });
  }

  ngOnInit(): void {

    const today = new Date();
    this.years18 = new Date();
    this.years18.setDate(today.getDate());
    this.years18.setMonth(today.getMonth());
    this.years18.setFullYear(today.getFullYear() - 18);


    this.skillService.getAllSkills().subscribe(
      result => {
        this.skillsBackend = result;
        result.forEach((item, index) => {
          this.options.push(item.name);
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
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }

  addCandidate(): void {
    const fullNameInput = this.form.value.fullNameInput;
    const emailInput = this.form.value.emailInput;
    const contactNumberInput = this.form.value.contactNumberInput;
    const dateInput = this.form.value.dateInput;
    const skillList = [];
    this.skills.forEach((item, index) => {
      skillList.push(item.name);
    });
    if (fullNameInput !== '' && emailInput !== '' && contactNumberInput !== '' && dateInput) {
      const user = {fullName: fullNameInput, email: emailInput,
        contactNumber: contactNumberInput, dateOfBirth: dateInput, skills: skillList};
      this.candidateService.addCandidate(user).subscribe(
        result => {
          this.snackBar.open('Successfully added new candidate!', 'Ok', {duration: 2000});
          this.router.navigate(['/']);
        },
        error => {
          this.snackBar.open('One of the fields is not unique!', 'Ok', {duration: 2000});
        }
      );
    }

  }

  onDelete($event: number): void {
    const newList = [];
    let skill;
    if ($event < 0){
      console.log(this.skills);
      this.skills.forEach((item, index) => {
        if (item.id !== $event){
          newList.push(item);
        }
      });
      console.log(newList);
      this.skills = newList;
    }else{
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

      const newOptions = this.options;
      newOptions.push(skill.name);
      this.options = newOptions;
    }
  }

  addSkill(): void {
    if (this.myControl.value !== null){
      const newList = [];
      let check = false;
      this.skills.forEach((item, index) => {
        newList.push(item);
        if (item.name === this.myControl.value){
          check = true;
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
        const newOptions = [];
        this.options.forEach((item, index) => {
          if (item !== this.myControl.value){
            newOptions.push(item);
          }
        });
        this.options = newOptions;
      }
    }
  }

}
