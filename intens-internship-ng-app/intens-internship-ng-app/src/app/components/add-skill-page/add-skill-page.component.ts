import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MatSnackBar} from '@angular/material/snack-bar';
import {SkillService} from '../../services/skill-service/skill.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-add-skill-page',
  templateUrl: './add-skill-page.component.html',
  styleUrls: ['./add-skill-page.component.css']
})
export class AddSkillPageComponent implements OnInit {

  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    public snackBar: MatSnackBar,
    private skillService: SkillService,
    private router: Router
  ) {
    this.form = this.fb.group({
      skillNameInput : [null, Validators.required],
    });
  }

  ngOnInit(): void {
  }

  addNewSkill(): void {
    const skillName = this.form.value.skillNameInput;
    this.skillService.addNewSkill({name: skillName}).subscribe(
      result => {
        this.snackBar.open('Successfully added new skill!', 'Ok', {duration: 2000});
        this.router.navigate(['/skills']);
      },
      error => {
        this.snackBar.open('Name must be unique!', 'Ok', {duration: 2000});
      }
    );
  }
}
