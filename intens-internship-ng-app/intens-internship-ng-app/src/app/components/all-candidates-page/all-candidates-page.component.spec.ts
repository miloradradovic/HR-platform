import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllCandidatesPageComponent } from './all-candidates-page.component';

describe('AllCandidatesPageComponent', () => {
  let component: AllCandidatesPageComponent;
  let fixture: ComponentFixture<AllCandidatesPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllCandidatesPageComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AllCandidatesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
