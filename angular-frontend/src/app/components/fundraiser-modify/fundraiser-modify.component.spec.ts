import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FundraiserModifyComponent } from './fundraiser-modify.component';

describe('FundraiserModifyComponent', () => {
  let component: FundraiserModifyComponent;
  let fixture: ComponentFixture<FundraiserModifyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FundraiserModifyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FundraiserModifyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
