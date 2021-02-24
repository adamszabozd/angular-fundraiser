import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConvertedTransferAmountComponent } from './converted-transfer-amount.component';

describe('ConvertedTransferAmountComponent', () => {
  let component: ConvertedTransferAmountComponent;
  let fixture: ComponentFixture<ConvertedTransferAmountComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConvertedTransferAmountComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConvertedTransferAmountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
