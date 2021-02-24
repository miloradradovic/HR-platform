import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddSkillPageComponent } from './add-skill-page.component';

describe('AddSkillPageComponent', () => {
  let component: AddSkillPageComponent;
  let fixture: ComponentFixture<AddSkillPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddSkillPageComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddSkillPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
