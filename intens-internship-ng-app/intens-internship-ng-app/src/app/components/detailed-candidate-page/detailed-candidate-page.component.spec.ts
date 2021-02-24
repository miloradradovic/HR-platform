import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailedCandidatePageComponent } from './detailed-candidate-page.component';

describe('DetailedCandidatePageComponent', () => {
  let component: DetailedCandidatePageComponent;
  let fixture: ComponentFixture<DetailedCandidatePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetailedCandidatePageComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailedCandidatePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
