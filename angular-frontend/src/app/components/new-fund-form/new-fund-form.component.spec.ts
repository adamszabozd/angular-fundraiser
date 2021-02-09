import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewFundFormComponent } from './new-fund-form.component';

describe('NewFundFormComponent', () => {
  let component: NewFundFormComponent;
  let fixture: ComponentFixture<NewFundFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewFundFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewFundFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
