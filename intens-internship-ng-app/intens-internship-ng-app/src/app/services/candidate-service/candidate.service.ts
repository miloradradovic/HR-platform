import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {AbstractControl, ValidationErrors} from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class CandidateService {

  constructor(private http: HttpClient) { }

  getAllCandidates(): Observable<any> {
    return this.http.get('http://localhost:8080/candidates');
  }

  deleteCandidate($event: number): Observable<any> {
    return this.http.delete('http://localhost:8080/candidates/' + $event);
  }

  search(searchParams: { param: string; value: string }): Observable<any> {
    return this.http.post('http://localhost:8080/candidates/search', searchParams);
  }

  addCandidate(user: { skills: any[]; contactNumber: (string | ((control: AbstractControl) => (ValidationErrors | null)))[];
  fullName: (string | ((control: AbstractControl) => (ValidationErrors | null)))[]; dateOfBirth: any;
  email: (string | ((control: AbstractControl) => (ValidationErrors | null)))[] }): Observable<any> {
    return this.http.post('http://localhost:8080/candidates', user);
  }

  getCandidateById(id: number): Observable<any> {
    return this.http.get('http://localhost:8080/candidates/by-id/' + id);
  }

  updateCandidate(candidate: any): Observable<any> {
    return this.http.put('http://localhost:8080/candidates', candidate);
  }

  removeSkillFromCandidate(idPassed: number, id): Observable<any> {
    return this.http.delete('http://localhost:8080/candidates/' + idPassed + '/' + id);
  }
}
