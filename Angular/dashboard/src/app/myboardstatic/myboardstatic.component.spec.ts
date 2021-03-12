import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyboardstaticComponent } from './myboardstatic.component';

describe('MyboardstaticComponent', () => {
  let component: MyboardstaticComponent;
  let fixture: ComponentFixture<MyboardstaticComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MyboardstaticComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MyboardstaticComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
