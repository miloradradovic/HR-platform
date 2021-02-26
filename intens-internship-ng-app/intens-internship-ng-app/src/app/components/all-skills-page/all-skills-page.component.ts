import { Component, OnInit } from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';
import {SkillService} from '../../services/skill-service/skill.service';

@Component({
  selector: 'app-all-skills-page',
  templateUrl: './all-skills-page.component.html',
  styleUrls: ['./all-skills-page.component.css']
})
export class AllSkillsPageComponent implements OnInit {

  skills = [];

  constructor(private snackBar: MatSnackBar,
              private skillService: SkillService) { }

  ngOnInit(): void {
    this.setupData();
  }

  private setupData(): void{
    this.skillService.getAllSkills().subscribe(
      result => {
        this.skills = result;
      },
      error => {
        this.snackBar.open('Something went wrong!', 'Ok', {duration: 2000});
      }
    );
  }

  onDelete($event: number): void {
    this.skillService.deleteSkill($event).subscribe(
      result => {
        this.snackBar.open('Successfully deleted!', 'Ok', {duration: 2000});
        const newList = [];
        this.skills.forEach((item, index) => {
          if (item.id !== $event){
            newList.push(item);
          }
        });
        this.skills = newList;
      },
      error => {

      }
    );
  }
}
