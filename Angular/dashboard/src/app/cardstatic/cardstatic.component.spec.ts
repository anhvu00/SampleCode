import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardstaticComponent } from './cardstatic.component';

describe('CardstaticComponent', () => {
  let component: CardstaticComponent;
  let fixture: ComponentFixture<CardstaticComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CardstaticComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CardstaticComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
