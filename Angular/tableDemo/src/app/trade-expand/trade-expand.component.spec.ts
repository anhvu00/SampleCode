import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TradeExpandComponent } from './trade-expand.component';

describe('TradeExpandComponent', () => {
  let component: TradeExpandComponent;
  let fixture: ComponentFixture<TradeExpandComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TradeExpandComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TradeExpandComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
