import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllSkillsPageComponent } from './all-skills-page.component';

describe('AllSkillsPageComponent', () => {
  let component: AllSkillsPageComponent;
  let fixture: ComponentFixture<AllSkillsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllSkillsPageComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AllSkillsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
