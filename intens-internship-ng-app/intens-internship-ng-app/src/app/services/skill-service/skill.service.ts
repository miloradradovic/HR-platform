import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {AbstractControl, ValidationErrors} from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class SkillService {

  constructor(private http: HttpClient) { }

  getAllSkills(): Observable<any> {
    return this.http.get('http://localhost:8080/skills');
  }

  addNewSkill(param: { name: (null | ((control: AbstractControl) => (ValidationErrors | null)))[] }): Observable<any> {
    return this.http.post('http://localhost:8080/skills', param);
  }

  deleteSkill($event: number): Observable<any> {
    return this.http.delete('http://localhost:8080/skills/' + $event);
  }
}
