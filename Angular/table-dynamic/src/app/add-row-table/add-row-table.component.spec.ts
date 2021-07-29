import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRowTableComponent } from './add-row-table.component';

describe('AddRowTableComponent', () => {
  let component: AddRowTableComponent;
  let fixture: ComponentFixture<AddRowTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddRowTableComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddRowTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
