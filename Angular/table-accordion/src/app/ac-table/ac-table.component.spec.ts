import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AcTableComponent } from './ac-table.component';

describe('AcTableComponent', () => {
  let component: AcTableComponent;
  let fixture: ComponentFixture<AcTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AcTableComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AcTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
