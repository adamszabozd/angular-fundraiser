import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConvertedBalanceComponent } from './converted-balance.component';

describe('ConvertedBalanceComponent', () => {
  let component: ConvertedBalanceComponent;
  let fixture: ComponentFixture<ConvertedBalanceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConvertedBalanceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConvertedBalanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
